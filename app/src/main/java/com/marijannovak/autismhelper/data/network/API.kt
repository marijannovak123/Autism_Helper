package com.marijannovak.autismhelper.data.network

import com.marijannovak.autismhelper.data.models.*
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

    @GET("child_scores/{childId}.json")
    fun getChildScores(@Path("childId") childId : Int) : Single<List<ChildScore>>

    @GET("users/{userId}.json")
    fun getUser(@Path("userId") userId : String) : Single<User>

    @PUT("users/{userId}.json")
    fun putUser(@Path("userId") userId : String, @Body user : User) : Completable

    @PUT("users/{userId}/child_scores/{scoreId}.json")
    fun putScore(@Path("userId") userId: String, @Path("scoreId") scoreId: Int, @Body score: ChildScore): Completable

    @PATCH("users/{userId}/children/{childId}.json")
    fun addChild(@Path("userId") userId: Int, @Path("childId") childId: Int, @Body child: Child)
}