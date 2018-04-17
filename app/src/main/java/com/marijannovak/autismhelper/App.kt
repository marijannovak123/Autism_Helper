package com.marijannovak.autismhelper

import android.app.Activity
import android.app.Application
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.config.Constants.Companion.DB_NAME
import com.marijannovak.autismhelper.config.Constants.Companion.PREFS_NAME
import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.di.DaggerAppComponent
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
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return supportFragmentInjector
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
                    if(ViewModelActivity::class.java.isAssignableFrom(it.javaClass)) {
                        AndroidInjection.inject(activity)

                        if (it is FragmentActivity) {
                            it.supportFragmentManager
                                .registerFragmentLifecycleCallbacks(
                                        object : FragmentManager.FragmentLifecycleCallbacks() {
                                            override fun onFragmentCreated(fm: FragmentManager?, f: Fragment?, savedInstanceState: Bundle?) {
                                                f?.let {
                                                    if (BaseFragment::class.java.isAssignableFrom(f.javaClass)) {
                                                        AndroidSupportInjection.inject(f)
                                                    }
                                                }
                                            }
                                        }, true)
                    }
                }
            }

        }})
    }

    companion object {
        private var databaseInstance : AppDatabase? = null
        private lateinit var context: App

        fun getDBInstance() : AppDatabase {
            if(databaseInstance == null) {
                databaseInstance = Room.databaseBuilder(context, AppDatabase::class.java,DB_NAME).build()
            }
                return databaseInstance!!
        }

        fun closeDB() {
            this.databaseInstance = null
        }

        fun getAppContext() = context
     }
}