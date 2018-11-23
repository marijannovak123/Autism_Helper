package com.marijannovak.autismhelper.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_USER
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
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
    fun getCurrentUser(): User

    @Query("DELETE FROM $TABLE_USER")
    fun deleteTable()

    @Query("SELECT * FROM $TABLE_USER")
    fun getAllUsers(): Single<List<User>>

    @Query("SELECT * FROM $TABLE_USER LIMIT 1")
    fun getUserRaw(): User

    @Transaction
    fun updateAll(userName: String, parentPassword: String, profilePicPath: String) {
        val user = getUserRaw()
        user.username = userName
        user.parentPassword = parentPassword
        user.profilePicPath = profilePicPath
        insert(user)
    }

    @Transaction
    fun update(userName: String, parentPassword: String) {
        val user = getUserRaw()
        user.username = userName
        user.parentPassword = parentPassword
        insert(user)
    }
}