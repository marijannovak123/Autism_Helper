package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import android.util.Log
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_USER
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.data.models.UserUpdateRequest
import com.marijannovak.autismhelper.utils.logTag
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface UserDao : BaseDao<User> {

    @Transaction
    @Query("SELECT * FROM $TABLE_USER limit 1")
    fun getUserWithChildren(): Flowable<UserChildrenJoin>

    @Query("SELECT * FROM $TABLE_USER limit 1")
    fun userLoggedIn(): Maybe<User>

    @Query("SELECT * FROM $TABLE_USER limit 1")
    fun getCurrentUser(): Flowable<User>

    @Query("SELECT * FROM $TABLE_USER limit 1")
    fun getCurrentUserSingle(): Single<User>

    @Query("DELETE FROM $TABLE_USER")
    fun deleteTable()

    @Query("SELECT * FROM $TABLE_USER")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM $TABLE_USER LIMIT 1")
    fun getUserRaw(): User

    @Transaction
    fun update(userName: String, parentPassword: String, profilePicPath: String) {
        val user = getUserRaw()
        Log.e(logTag(), user.username + " " +  user.profilePicPath)
        user.username = userName
        user.parentPassword = parentPassword
        user.profilePicPath = profilePicPath
        Log.e(logTag(), user.username + " " +  user.profilePicPath)
        insert(user)
        val users = getAllUsers()
    }
}