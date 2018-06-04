package com.marijannovak.autismhelper.testinjection

import com.marijannovak.autismhelper.di.ActivityModule
import com.marijannovak.autismhelper.di.FragmentModule
import com.marijannovak.autismhelper.di.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidInjectionModule::class),
    (ActivityModule::class), (FragmentModule::class), (ViewModelModule::class),
    (TestAppModule::class), (TestNetworkModule::class), (TestDbModule::class)])
interface TestAppComponent {
    fun inject(app: TestApp)
}