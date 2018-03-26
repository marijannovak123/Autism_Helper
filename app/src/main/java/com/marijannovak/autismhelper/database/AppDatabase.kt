package com.marijannovak.autismhelper.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.database.dao.*
import com.marijannovak.autismhelper.models.*

/**
 * Created by Marijan on 26.3.2018..
 */

@Database(entities = [(User::class), (Question::class),
                     (Answer::class), (Category::class),
                     (QuestionType::class)],
        version = 1)

abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao
    abstract fun questionDao() : QuestionDao
    abstract fun answerDao() : AnswerDao
    abstract fun categoriesDao() : CategoryDao
    abstract fun questionTypeDao() : QuestionTypeDao

    companion object {
        fun getUserDao() = App.getDBInstance().userDao()
        fun getQuestionDao() = App.getDBInstance().questionDao()
        fun getAnswerDao() = App.getDBInstance().answerDao()
        fun getCategoriesDao() = App.getDBInstance().categoriesDao()
        fun getQuestionTypeDao() = App.getDBInstance().questionTypeDao()

        fun closeDB(){
            App.closeDB()
        }
    }
}
