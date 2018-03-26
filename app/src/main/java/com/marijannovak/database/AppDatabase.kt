package com.marijannovak.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.models.User

/**
 * Created by Marijan on 26.3.2018..
 */

@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao

    companion object {
        fun getUserDao() = App.getDBInstance().userDao()

    }
}
