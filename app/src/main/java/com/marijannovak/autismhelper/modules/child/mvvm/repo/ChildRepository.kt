package com.marijannovak.autismhelper.modules.child.mvvm.repo

import com.marijannovak.autismhelper.data.database.dao.CategoryDao
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.utils.handleThreading
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChildRepository @Inject constructor(private val categoriesDao: CategoryDao) {
    fun loadCategories(): Flowable<List<Category>> {
        return categoriesDao
                .getCategories()
                .handleThreading()
    }
}