package com.marijannovak.autismhelper.testinjection

import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.di.DbModule
import org.mockito.Mockito

class TestDbModule: DbModule() {
    override fun provideDb(app: App): AppDatabase {
        return Mockito.mock(AppDatabase::class.java)
    }
}