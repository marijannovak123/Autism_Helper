package com.marijannovak.autismhelper.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.data.database.dao.*
import com.marijannovak.autismhelper.data.database.typeconverters.AnswerConverter
import com.marijannovak.autismhelper.data.database.typeconverters.ChildConverter
import com.marijannovak.autismhelper.data.models.*

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
}
