package com.marijannovak.autismhelper.data.repo

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.data.models.ParentPasswordRequest
import com.marijannovak.autismhelper.data.models.Question
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.PrefsHelper
import com.marijannovak.autismhelper.utils.handleThreading
import com.marijannovak.autismhelper.utils.logTag
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.doAsync
import java.io.File
import javax.inject.Inject

/**
 * Created by Marijan on 26.3.2018..
 */
class DataRepository @Inject constructor(
        private val api: API,
        private val auth: FirebaseAuth,
        private val storage: StorageReference,
        private val db: AppDatabase,
        private val sharedPrefs: PrefsHelper) {

    private var questionsWithImgs: List<Question> = ArrayList()
    private lateinit var onImgsDownloaded: () -> Unit

    fun syncData(): Completable {
        return Completable.mergeArray(
                api.getCategories()
                        .doOnSuccess {
                            db.categoriesDao().insertMultiple(it)
                        }.toCompletable(),
                api.getQuestions()
                        .doOnSuccess {
                            for(question: Question in it) {
                                db.questionDao().insert(question)
                                db.answerDao().insertMultiple(question.answers)

                                if (question.categoryId == 2) {
                                    questionsWithImgs += question
                                }
                            }
                        }.toCompletable()
        ).handleThreading()
    }

    fun logOut(): Completable {
        auth.signOut()
        sharedPrefs.setParentPassword("")
        return Completable.fromAction {
            doAsync {
                db.userDao().deleteTable()
                db.childDao().deleteTable()
                db.childScoreDao().deleteTable()
                deleteDataTables()
            }
        }
    }

    private fun deleteDataTables() {
        db.questionDao().deleteTable()
        db.categoriesDao().deleteTable()
    }

    fun getParentPassword(): String = sharedPrefs.getParentPassword()

    fun saveParentPassword(password: String): Completable {
        sharedPrefs.setParentPassword(password)

        return db.userDao().getCurrentUser().flatMapCompletable { user ->
            Completable.mergeArray(
                    api.updateParentPassword(user.id, ParentPasswordRequest(password)),
                    Completable.fromAction {
                        db.userDao().insert(user)
                    }
            )
        }.handleThreading()
    }

    fun loadUserAndChildren(): Flowable<UserChildrenJoin> {
        return db.userDao()
                .getUserWithChildren()
                .handleThreading()
    }

    fun downloadImages(onComplete: () -> Unit) {
        this.onImgsDownloaded = onComplete
        downloadImage(0)
    }

    fun downloadImage(pos: Int) {
        Log.e(logTag(), "Downloading ${questionsWithImgs[pos].extraData}")
        val question = questionsWithImgs[pos]
        val ref = storage.child(question.extraData!!)
        val file = File.createTempFile("img", ".jpg")

        ref.getFile(file)
                .addOnSuccessListener {
                    updateQuestionImgPath(question, file.absolutePath).subscribe( object: CompletableObserver {
                        override fun onComplete() {
                            if(pos == questionsWithImgs.size-1) {
                                onImgsDownloaded()
                            } else {
                                downloadImage(pos+1)
                            }
                        }

                        override fun onSubscribe(d: Disposable?) {
                            //
                        }

                        override fun onError(e: Throwable?) {
                            Log.e(logTag(), "FAIL ${question.extraData!!}")
                        }
                    })
                }
                .addOnFailureListener {
                    Log.e(logTag(), "FAIL ${questionsWithImgs[pos].extraData!!}")
                }
    }

    private fun updateQuestionImgPath(question: Question, path: String): Completable {
        question.imgPath = path
        return Completable.fromAction {
            doAsync {
                db.questionDao().insert(question)
            }
        }
    }

}