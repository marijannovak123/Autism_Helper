package com.marijannovak.autismhelper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.marijannovak.autismhelper.config.Constants.Companion.PREFS_NAME
import com.marijannovak.autismhelper.di.AppComponent
import com.marijannovak.autismhelper.di.DaggerAppComponent
import com.marijannovak.autismhelper.utils.isInjectable
import com.marijannovak.autismhelper.utils.isInjectableFragment
import com.tumblr.remember.Remember
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by Marijan on 26.3.2018..
 */
class App: Application(), HasActivityInjector, HasSupportFragmentInjector {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment> {
        return fragmentInjector
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        Remember.init(this, PREFS_NAME)

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build()

        appComponent.inject(this)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
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
                    if (it.isInjectable()) {
                        AndroidInjection.inject(activity)
                    }

                    if (it is androidx.fragment.app.FragmentActivity) {
                        it.supportFragmentManager
                                .registerFragmentLifecycleCallbacks(
                                        object : androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks() {
                                            override fun onFragmentCreated(fm: androidx.fragment.app.FragmentManager, f: androidx.fragment.app.Fragment, savedInstanceState: Bundle?) {
                                                f?.let {
                                                    if (it.isInjectableFragment()) {
                                                        AndroidSupportInjection.inject(f)
                                                    }
                                                }
                                            }
                                        }, true)
                    }
                }
            }
        })

    }

    companion object {
        private lateinit var context: App

        fun getAppContext() = context
    }
}