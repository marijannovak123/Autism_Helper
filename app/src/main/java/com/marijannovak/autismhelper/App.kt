package com.marijannovak.autismhelper

import android.app.Application
import android.arch.persistence.room.Room
import com.marijannovak.autismhelper.config.Constants.Companion.DB_NAME
import com.marijannovak.autismhelper.config.Constants.Companion.PREFS_NAME
import com.marijannovak.autismhelper.database.AppDatabase
import com.tumblr.remember.Remember

/**
 * Created by Marijan on 26.3.2018..
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this

        Remember.init(this, PREFS_NAME)
    }

    companion object {
        private var databaseInstance : AppDatabase? = null
        private lateinit var context: App

        fun getDBInstance() : AppDatabase {
            if(databaseInstance == null) {
                databaseInstance = Room.databaseBuilder(context, AppDatabase::class.java,DB_NAME).build()
            }
                return databaseInstance!!
        }

        fun closeDB() {
            this.databaseInstance = null
        }

        fun getAppContext() = context
     }
}