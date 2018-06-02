package com.marijannovak.autismhelper.modules.main.mvvm

import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.database.dao.ChildDao
import com.marijannovak.autismhelper.data.models.Child
import io.reactivex.Flowable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Marijan on 23.3.2018..
 */
@Singleton
class MainRepository @Inject constructor(
        private val childDao: ChildDao,
        @Named(Constants.SCHEDULER_IO) private val ioScheduler: Scheduler,
        @Named(Constants.SCHEDULER_MAIN) private val mainScheduler: Scheduler) {

    fun getChildren(): Flowable<List<Child>> {
        return childDao
                .getChildren()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }

}