package com.marijannovak.autismhelper.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.data.models.Question
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.PrefsHelper
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import javax.inject.Inject

/**
 * Created by Marijan on 26.3.2018..
 */
class DataRepository @Inject constructor(
        private val api: API,
        private val auth: FirebaseAuth,
        private val db: AppDatabase,
        private val sharedPrefs: PrefsHelper) {

    fun syncData(): Single<Boolean> {
        return api
                .getCategories()
                .flatMap {
                    categories: List<Category> ->
                        if(categories.isNotEmpty()) {
                            saveCategories(categories)
                            api.getQuestions()
                        } else Single.error(Throwable())
                }
                .flatMap {questions: List<Question> ->
                        if(questions.isNotEmpty()) {
                            saveQuestions(questions)
                            Single.just(true)
                        } else Single.error(Throwable())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun logOut(): Completable {
        auth.signOut()
        sharedPrefs.setLoggedIn(false)
        sharedPrefs.setParentPassword("")
        return Completable.fromAction{
            doAsync {
                db.userDao().deleteTable()
                deleteDataTables()
            }
        }
    }

    fun deleteDataTables() {
        db.questionDao().deleteTable()
        db.categoriesDao().deleteTable()
    }

    private fun saveQuestions(questions: List<Question>)/*: Single<Any>*/ {
        doAsync {
            db.questionDao().insertMultiple(questions)

            for(question: Question in questions) {
                db.answerDao().insertMultiple(question.answers)
            }
        }
    }

    private fun saveCategories(categories: List<Category>)/*: Single<Any>*/ {
        doAsync {
            db.categoriesDao().insertMultiple(categories)
        }
    }

    fun getParentPassword(): String = sharedPrefs.getParentPassword()

    fun saveParentPassword(password: String) {
        sharedPrefs.setParentPassword(password)
    }

    fun loadUserAndChildren(): Single<UserChildrenJoin> {
        return db.userDao()
                .getUserChildren()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}