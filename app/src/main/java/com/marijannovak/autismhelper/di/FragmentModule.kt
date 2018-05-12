package com.marijannovak.autismhelper.di

import com.marijannovak.autismhelper.modules.parent.fragments.ChildDetailsFragment
import com.marijannovak.autismhelper.modules.parent.fragments.ChildrenFragment
import com.marijannovak.autismhelper.modules.parent.fragments.PhrasesFragment
import com.marijannovak.autismhelper.modules.parent.fragments.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    internal abstract fun provideChildrenFragment(): ChildrenFragment

    @ContributesAndroidInjector
    internal abstract fun provideChildDetailsFragment(): ChildDetailsFragment

    @ContributesAndroidInjector
    internal abstract fun providePhrasesFragment(): PhrasesFragment

    @ContributesAndroidInjector
    internal abstract fun provideProfileFragment(): ProfileFragment
}