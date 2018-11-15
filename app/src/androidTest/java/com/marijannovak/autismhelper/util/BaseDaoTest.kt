package com.marijannovak.autismhelper.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.marijannovak.autismhelper.data.database.AppDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseDaoTest {

    @JvmField @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    protected lateinit var db: AppDatabase

    @Before
     open fun setUp() {
        db = Room
                .inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    fun tearDown() {
        db.close()
    }
}
