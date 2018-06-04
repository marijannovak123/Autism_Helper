package com.marijannovak.autismhelper.testinjection

import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.data.database.dao.*
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
class TestDbModule {

    @Provides
    fun provideDb(app: App): AppDatabase {
        return Mockito.mock(AppDatabase::class.java)
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return Mockito.mock(UserDao::class.java)
    }

    @Provides
    fun providChildDao(db: AppDatabase): ChildDao {
        return Mockito.mock(ChildDao::class.java)
    }

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao {
       return Mockito.mock(CategoryDao::class.java)
    }

    @Provides
    fun provideChildScoreDao(db: AppDatabase): ChildScoreDao {
        return Mockito.mock(ChildScoreDao::class.java)
    }

    @Provides
    fun provideQuestionDao(db: AppDatabase): QuestionDao {
        return Mockito.mock(QuestionDao::class.java)
    }

    @Provides
    fun provideAnswerDao(db: AppDatabase): AnswerDao {
        return Mockito.mock(AnswerDao::class.java)
    }

    @Provides
    fun provideAacDao(db: AppDatabase): AACDao {
        return Mockito.mock(AACDao::class.java)
    }

    @Provides
    fun provideRssDao(db: AppDatabase): FeedItemDao {
        return Mockito.mock(FeedItemDao::class.java)
    }
}