package com.marijannovak.autismhelper.di

import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.utils.PrefsHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class), (NetworkModule::class), (DbModule::class)])
class AppModule {

    @Singleton
    @Provides
    fun provideAppContext(): App {
        return App.getAppContext()
    }

    @Singleton
    @Provides
    fun provideSharedPrefs(): PrefsHelper {
        return PrefsHelper()
    }

}