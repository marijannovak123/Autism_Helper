package com.marijannovak.autismhelper.testinjection

import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.utils.PrefsHelper
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import org.mockito.Mockito
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [(TestViewModelModule::class)])
class TestAppModule {
    @Singleton
    @Provides
    fun provideAppContext(): App {
        return Mockito.mock(App::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedPrefs(): PrefsHelper {
        return Mockito.mock(PrefsHelper::class.java)
    }

    @Singleton
    @Provides
    @Named(Constants.SCHEDULER_IO)
    fun provideIOScheduler(): Scheduler {
        return TestScheduler()
    }

    @Singleton
    @Provides
    @Named(Constants.SCHEDULER_MAIN)
    fun provideMainScheduler(): Scheduler {
        return TestScheduler()
    }
}