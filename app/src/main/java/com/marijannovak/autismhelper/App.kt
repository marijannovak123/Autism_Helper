package com.marijannovak.autismhelper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.marijannovak.autismhelper.config.Constants.Companion.PREFS_NAME
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
class App : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        Remember.init(this, PREFS_NAME)

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)

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

                    if (it is FragmentActivity) {
                        it.supportFragmentManager
                                .registerFragmentLifecycleCallbacks(
                                        object : FragmentManager.FragmentLifecycleCallbacks() {
                                            override fun onFragmentCreated(fm: FragmentManager?, f: Fragment?, savedInstanceState: Bundle?) {
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