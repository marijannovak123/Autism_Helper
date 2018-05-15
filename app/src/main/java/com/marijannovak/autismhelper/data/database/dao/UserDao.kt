package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_USER
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.data.models.UserUpdateRequest
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

    @Query("DELETE FROM $TABLE_USER")
    fun deleteTable()

    @Query("UPDATE $TABLE_USER SET username = :userName, parentPassword = :parentPassword WHERE 1")
    fun update(userName: String, parentPassword: String)

    //@Query("UPDATE $TABLE_USER SET profileImgPath = :path WHERE 1")
    //fun updateUserProfileImg(path: String)
}