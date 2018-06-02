package com.marijannovak.autismhelper.di

import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.config.Constants.Companion.SCHEDULER_IO
import com.marijannovak.autismhelper.config.Constants.Companion.SCHEDULER_MAIN
import com.marijannovak.autismhelper.utils.PrefsHelper
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
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

    @Singleton
    @Provides
    @Named(SCHEDULER_IO)
    fun provideIOScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Singleton
    @Provides
    @Named(SCHEDULER_MAIN)
    fun provideMainScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}