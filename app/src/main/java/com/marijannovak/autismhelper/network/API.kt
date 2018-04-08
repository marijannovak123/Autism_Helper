package com.marijannovak.autismhelper.network

import com.marijannovak.autismhelper.models.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

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

    @GET("child_scores/{childId}.json")
    fun getChildScores(@Path("childId") childId : Int) : Single<List<ChildScore>>

    @GET("users/{userId}.json")
    fun getUser(@Path("userId") userId : String) : Single<User>

    @PUT("users/{userId}.json")
    fun putUser(@Path("userId") userId : String, @Body user : User) : Completable

    @PUT("child_scores/{childId).json")
    fun putChildScore(@Path("childId") childId: Int, @Body childScore: ChildScore)

    @PATCH("users/{userId}/children/{childId}.json")
    fun addChild(@Path("userId") userId: Int, @Path("childId") childId: Int, @Body child: Child)
}