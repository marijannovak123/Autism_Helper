package com.marijannovak.autismhelper.network

import com.marijannovak.autismhelper.models.Category
import com.marijannovak.autismhelper.models.Question
import com.marijannovak.autismhelper.models.QuestionType
import com.marijannovak.autismhelper.models.User
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by Marijan on 22.3.2018..
 */
interface API {

    @GET("questions.json")
    fun getQuestions() : Single<List<Question>>

    @GET("categories.json")
    fun getCategories() : Single<List<Category>>

    @GET("question-types.json")
    fun getQuestionTypes() : Single<List<QuestionType>>

    @GET("user.json")
    fun getUser(username : String) : Single<User>
}