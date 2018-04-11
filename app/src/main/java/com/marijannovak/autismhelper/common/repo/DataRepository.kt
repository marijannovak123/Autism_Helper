package com.marijannovak.autismhelper.common.repo

import com.google.firebase.auth.FirebaseAuth
import com.marijannovak.autismhelper.database.AppDatabase
import com.marijannovak.autismhelper.models.Category
import com.marijannovak.autismhelper.models.Question
import com.marijannovak.autismhelper.models.QuestionType
import com.marijannovak.autismhelper.network.APIService
import com.marijannovak.autismhelper.utils.PrefsHelper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by Marijan on 26.3.2018..
 */
class DataRepository : IDataRepository {
    override fun syncData(): Single<Boolean> {
        return APIService
                .getApi()
                .getCategories()
                .flatMap {
                    categories: List<Category> ->
                        if(categories.isNotEmpty()) {
                            saveCategories(categories)
                            APIService.getApi().getQuestionTypes()
                        } else Single.error(Throwable("Category sync failed"))
                }
                .flatMap {
                    questionTypes: List<QuestionType> ->
                        if(questionTypes.isNotEmpty()) {
                            saveQuestionTypes(questionTypes)
                            APIService.getApi().getQuestions()
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

    override fun logOut() {
        val authService = FirebaseAuth.getInstance()
        authService.signOut()

        doAsync {
            AppDatabase.getUserDao().deleteTable()
        }

        PrefsHelper.setLoggedIn(false)
    }

    override fun deleteDataTables() {
        doAsync {
            AppDatabase.getQuestionDao().deleteTable()
            AppDatabase.getCategoriesDao().deleteTable()
            AppDatabase.getQuestionTypeDao().deleteTable()
        }
    }

    private fun saveQuestions(questions: List<Question>) {
        doAsync {
            AppDatabase.getQuestionDao().saveQuestions(questions)
        }
    }

    private fun saveQuestionTypes(questionTypes: List<QuestionType>) {
        doAsync {
            AppDatabase.getQuestionTypeDao().saveQuestionTypes(questionTypes)
        }
    }

    private fun saveCategories(categories: List<Category>) {
        doAsync {
            AppDatabase.getCategoriesDao().saveCategories(categories)
        }
    }

    override fun getParentPassword(): String = PrefsHelper.getParentPassword()

    override fun saveParentPassword(password: String) {
        PrefsHelper.setParentPassword(password)
    }
}