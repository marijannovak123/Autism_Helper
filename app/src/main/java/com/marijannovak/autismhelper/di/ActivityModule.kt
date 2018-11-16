package com.marijannovak.autismhelper.di

import com.marijannovak.autismhelper.ui.activities.AACActivity
import com.marijannovak.autismhelper.ui.activities.PickCategoryActivity
import com.marijannovak.autismhelper.ui.activities.QuizActivity
import com.marijannovak.autismhelper.ui.activities.LoginActivity
import com.marijannovak.autismhelper.ui.activities.SignUpActivity
import com.marijannovak.autismhelper.ui.activities.MainActivity
import com.marijannovak.autismhelper.ui.activities.ParentActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun provideLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    internal abstract fun provideSignupActivity(): SignUpActivity

    @ContributesAndroidInjector
    internal abstract fun provideMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun provideParentActivity(): ParentActivity

    @ContributesAndroidInjector
    internal abstract fun providePickCategoryActivity(): PickCategoryActivity

    @ContributesAndroidInjector
    internal abstract fun provideQuizActivity(): QuizActivity

    @ContributesAndroidInjector
    internal abstract fun provideAacActivity(): AACActivity

}