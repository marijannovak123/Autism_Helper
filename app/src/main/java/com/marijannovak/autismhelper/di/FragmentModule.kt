package com.marijannovak.autismhelper.di

import com.marijannovak.autismhelper.ui.fragments.*
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

    @ContributesAndroidInjector
    internal abstract fun provideSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    internal abstract fun provideRssFragment(): RssFragment

    @ContributesAndroidInjector
    internal abstract fun providePhraseCategoryFragment(): PhraseCategoryFragment

    @ContributesAndroidInjector
    internal abstract fun providePhrasePickFragment(): PhrasePickFragment

    @ContributesAndroidInjector
    internal abstract fun provideSentencePickFragment(): SentencePickFragment

}