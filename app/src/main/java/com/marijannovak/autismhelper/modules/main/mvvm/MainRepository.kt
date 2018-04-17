package com.marijannovak.autismhelper.modules.main.mvvm

import com.marijannovak.autismhelper.data.database.dao.CategoryDao
import com.marijannovak.autismhelper.data.models.Category
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Marijan on 23.3.2018..
 */
class MainRepository @Inject constructor(private val categoriesDao: CategoryDao){

    fun loadCategories(): Flowable<List<Category>> {
        return categoriesDao
                .getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}