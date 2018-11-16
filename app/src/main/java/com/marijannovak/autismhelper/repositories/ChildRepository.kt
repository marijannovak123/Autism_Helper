package com.marijannovak.autismhelper.repositories

import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.database.dao.CategoryDao
import com.marijannovak.autismhelper.data.models.Category
import io.reactivex.Flowable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ChildRepository @Inject constructor(
        private val categoriesDao: CategoryDao,
        @Named(Constants.SCHEDULER_IO) private val ioScheduler: Scheduler,
        @Named(Constants.SCHEDULER_MAIN) private val mainScheduler: Scheduler) {
    fun loadCategories(): Flowable<List<Category>> {
        return categoriesDao
                .getCategories()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }
}