package com.marijannovak.autismhelper.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_USER
import com.marijannovak.autismhelper.models.User
import io.reactivex.Maybe

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM $TABLE_USER limit 1")
    fun getUser() : Maybe<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user : User)

    @Delete
    fun delete(user : User)

}