package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_USER
import com.marijannovak.autismhelper.data.models.User
import io.reactivex.Single

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM $TABLE_USER limit 1")
    fun getUser() : Single<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user : User)

    @Delete
    fun delete(user : User)

    @Query("DELETE FROM $TABLE_USER")
    fun deleteTable()
}