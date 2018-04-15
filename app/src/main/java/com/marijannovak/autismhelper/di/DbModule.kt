package com.marijannovak.autismhelper.di

import android.arch.persistence.room.Room
import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.data.database.dao.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {
    @Singleton
    @Provides
    fun provideDb(app: App): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, Constants.DB_NAME).build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao {
        return db.categoriesDao()
    }

    @Singleton
    @Provides
    fun provideChildScoreDao(db: AppDatabase): ChildScoreDao {
        return db.childScoreDao()
    }

    @Singleton
    @Provides
    fun provideQuestionDao(db: AppDatabase): QuestionDao {
        return db.questionDao()
    }

    @Singleton
    @Provides
    fun provideQuestionTypeDao(db: AppDatabase): QuestionTypeDao {
        return db.questionTypeDao()
    }
}