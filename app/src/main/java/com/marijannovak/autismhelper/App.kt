package com.marijannovak.autismhelper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.marijannovak.autismhelper.config.Constants.Companion.PREFS_NAME
import com.marijannovak.autismhelper.di.DaggerAppComponent
import com.marijannovak.autismhelper.utils.isViewModelActivity
import com.tumblr.remember.Remember
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by Marijan on 26.3.2018..
 */
class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        Remember.init(this, PREFS_NAME)

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)

        registerActivityLifecycleCallbacks(object: ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
                //NOOP
            }

            override fun onActivityResumed(activity: Activity?) {
                //NOOP
            }

            override fun onActivityStarted(activity: Activity?) {
                //NOOP
            }

            override fun onActivityDestroyed(activity: Activity?) {
                //NOOP
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                //NOOP
            }

            override fun onActivityStopped(activity: Activity?) {
                //NOOP
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                activity?.let {
                    if (it.isViewModelActivity()) {
                        AndroidInjection.inject(activity)
                    }
                }
            }})

    }

    companion object {
        private lateinit var context: App

        fun getAppContext() = context
     }
}