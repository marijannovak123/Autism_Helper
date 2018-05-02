package com.marijannovak.autismhelper.di

import com.marijannovak.autismhelper.modules.child.PickCategoryActivity
import com.marijannovak.autismhelper.modules.child.QuizActivity
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.modules.login.SignUpActivity
import com.marijannovak.autismhelper.modules.main.MainActivity
import com.marijannovak.autismhelper.modules.parent.ParentActivity
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

}