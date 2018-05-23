package com.marijannovak.autismhelper.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.marijannovak.autismhelper.data.database.dao.*
import com.marijannovak.autismhelper.data.models.*

/**
 * Created by Marijan on 26.3.2018..
 */

@Database(entities = [(User::class), (Child::class), (ChildScore::class),
    (Question::class), (Answer::class), (Category::class), (AacPhrase::class)],
        version = 10, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun childDao(): ChildDao
    abstract fun childScoreDao(): ChildScoreDao
    abstract fun questionDao(): QuestionDao
    abstract fun answerDao(): AnswerDao
    abstract fun categoriesDao(): CategoryDao
    abstract fun aacDao(): AACDao
}
