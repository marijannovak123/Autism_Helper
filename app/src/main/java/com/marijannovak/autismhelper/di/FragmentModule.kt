package com.marijannovak.autismhelper.di

import com.marijannovak.autismhelper.modules.parent.fragments.ChildrenFragment
import com.marijannovak.autismhelper.modules.parent.fragments.DashboardFragment
import com.marijannovak.autismhelper.modules.parent.fragments.ProfileFragment
import com.marijannovak.autismhelper.modules.parent.fragments.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    internal abstract fun provideSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    internal abstract fun provideDashboardFragment(): DashboardFragment

    @ContributesAndroidInjector
    internal abstract fun provideProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    internal abstract fun provideChildrenFragment(): ChildrenFragment

}