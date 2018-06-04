package com.marijannovak.autismhelper.testinjection

import com.marijannovak.autismhelper.di.AppComponent
import com.marijannovak.autismhelper.di.AppModule
import com.marijannovak.autismhelper.di.DbModule
import com.marijannovak.autismhelper.di.NetworkModule
import com.marijannovak.autismhelper.util.BaseInstrumentedTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (DbModule::class), (NetworkModule::class)])
interface TestAppComponent : AppComponent {
    fun inject(test: BaseInstrumentedTest)
}