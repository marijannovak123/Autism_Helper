package com.marijannovak.autismhelper.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.data.database.dao.CategoryDao
import com.marijannovak.autismhelper.data.database.dao.QuestionDao
import com.marijannovak.autismhelper.data.database.dao.QuestionTypeDao
import com.marijannovak.autismhelper.data.database.dao.UserDao
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.data.models.Question
import com.marijannovak.autismhelper.data.models.QuestionType
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.PrefsHelper
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
                            api.getQuestionTypes()
                        } else Single.error(Throwable("Category sync failed"))
                }
                .flatMap {
                    questionTypes: List<QuestionType> ->
                        if(questionTypes.isNotEmpty()) {
                            saveQuestionTypes(questionTypes)
                            api.getQuestions()
                        } else Single.error(Throwable("Question type sync failed"))
                }
                .flatMap {questions: List<Question> ->
                        if(questions.isNotEmpty()) {
                            saveQuestions(questions)
                            Single.just(true)
                        } else Single.error(Throwable("Questions sync failed"))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun logOut() {
        auth.signOut()

        doAsync {
            db.userDao().deleteTable()
        }

        sharedPrefs.setLoggedIn(false)
    }

    fun deleteDataTables() {
        doAsync {
            db.questionDao().deleteTable()
            db.categoriesDao().deleteTable()
            db.questionTypeDao().deleteTable()
        }
    }

    private fun saveQuestions(questions: List<Question>) {
        doAsync {
            db.questionDao().saveMultiple(questions)
        }
    }

    private fun saveQuestionTypes(questionTypes: List<QuestionType>) {
        doAsync {
            db.questionTypeDao().saveMultiple(questionTypes)
        }
    }

    private fun saveCategories(categories: List<Category>) {
        doAsync {
            db.categoriesDao().saveMultiple(categories)
        }
    }

    fun getParentPassword(): String = sharedPrefs.getParentPassword()

    fun saveParentPassword(password: String) {
        sharedPrefs.setParentPassword(password)
    }
}