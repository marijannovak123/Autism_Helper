package com.marijannovak.autismhelper.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.database.dao.*
import com.marijannovak.autismhelper.database.typeconverters.AnswerConverter
import com.marijannovak.autismhelper.database.typeconverters.ChildConverter
import com.marijannovak.autismhelper.models.*

/**
 * Created by Marijan on 26.3.2018..
 */

@Database(entities = [(User::class), (ChildScore::class),
                      (Question::class), (Category::class), (QuestionType::class)],
        version = 1, exportSchema = false)
@TypeConverters(AnswerConverter::class, ChildConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao
    abstract fun childScoreDao() : ChildScoreDao
    abstract fun questionDao() : QuestionDao
    abstract fun categoriesDao() : CategoryDao
    abstract fun questionTypeDao() : QuestionTypeDao

    companion object {
        fun getUserDao() = App.getDBInstance().userDao()
        fun getChildScoreDao() = App.getDBInstance().childScoreDao()
        fun getQuestionDao() = App.getDBInstance().questionDao()
        fun getCategoriesDao() = App.getDBInstance().categoriesDao()
        fun getQuestionTypeDao() = App.getDBInstance().questionTypeDao()

        fun closeDB(){
            App.closeDB()
        }
    }
}
