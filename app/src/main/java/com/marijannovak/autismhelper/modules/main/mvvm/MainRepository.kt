package com.marijannovak.autismhelper.modules.main.mvvm

import com.marijannovak.autismhelper.database.AppDatabase
import com.marijannovak.autismhelper.models.Category
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Marijan on 23.3.2018..
 */
class MainRepository : IMainRepository {

    override fun loadCategories() : Flowable<List<Category>> {
        return AppDatabase.getCategoriesDao()
                .getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}