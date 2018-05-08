package com.marijannovak.autismhelper.modules.child.mvvm.repo

import com.marijannovak.autismhelper.data.database.dao.CategoryDao
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.utils.handleThreading
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChildRepository @Inject constructor(private val categoriesDao: CategoryDao) {
    fun loadCategories(): Flowable<List<Category>> {
        return categoriesDao
                .getCategories()
                .handleThreading()
    }
}