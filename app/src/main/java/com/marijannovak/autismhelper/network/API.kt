package com.marijannovak.autismhelper.network

import com.marijannovak.autismhelper.models.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @GET("answers.json")
    fun getAnswers() : Single<List<Answer>>

    @GET("users/{userId}.json")
    fun getUser(@Path("userId") userId : String) : Single<User>

    @PUT("users/{userId}.json")
    fun putUser(@Path("userId") userId : String, @Body user : User) : Completable

}