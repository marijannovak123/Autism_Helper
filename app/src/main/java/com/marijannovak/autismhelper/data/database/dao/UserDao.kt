package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_USER
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import io.reactivex.Single

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface UserDao: BaseDao<User> {

    @Transaction
    @Query("SELECT * FROM $TABLE_USER limit 1")
    fun getUser() : Single<UserChildrenJoin>

    @Query("DELETE FROM $TABLE_USER")
    fun deleteTable()
}