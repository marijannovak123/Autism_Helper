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
    fun getQuestions(): Single<List<Question>>

    @GET("categories.json")
    fun getCategories(): Single<List<Category>>

    @GET("phrases.json")
    fun getPhrases(): Single<List<AacPhrase>>

    @GET("users/{userId}.json")
    fun getUser(@Path("userId") userId: String): Single<User>

    @GET("users/{userId}/children.json")
    fun getChildren(@Path("userId") userId: String): Single<List<Child>>

    @GET("users/{userId}/child_scores.json")
    fun getChildScores(@Path("userId") userId: String): Single<List<ChildScore>>

    @PUT("users/{userId}.json")
    fun putUser(@Path("userId") userId: String, @Body user: User): Completable

    @PUT("users/{userId}/child_scores/{scoreIndex}.json")
    fun putScore(@Path("userId") userId: String, @Path("scoreIndex") scoreIndex: Int, @Body score: ChildScore): Completable

    @PUT("users/{userId}/children/{childIndex}.json")
    fun addChild(@Path("userId") userId: String, @Path("childIndex") childIndex: Int, @Body child: Child): Completable

    @PATCH("users/{userId}.json")
    fun updateParentPassword(@Path("userId") userId: String, @Body password: ParentPasswordRequest): Completable

    @PATCH("users/{userId}.json")
    fun updateParent(@Path("userId") userId: String, @Body user: UserUpdateRequest): Completable
}