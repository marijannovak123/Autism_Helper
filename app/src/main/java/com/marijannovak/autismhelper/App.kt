package com.marijannovak.autismhelper

import android.app.Application
import android.arch.persistence.room.Room
import com.marijannovak.database.AppDatabase

/**
 * Created by Marijan on 26.3.2018..
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        const val FIREBASE_API_KEY = "2Sm3l55sGpTgeojlAnyREqj5KOwcks1kx379XidA"
        private var databaseInstance : AppDatabase? = null
        private lateinit var context: App

        fun getDBInstance() : AppDatabase {
            if(databaseInstance == null) {
                databaseInstance = Room.databaseBuilder(context, AppDatabase::class.java,"autism-helper-db").build()
            }
                return databaseInstance!!
            }
     }
}