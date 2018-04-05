package com.marijannovak.autismhelper.modules.main.mvvm

import com.marijannovak.autismhelper.models.Category
import io.reactivex.Flowable

/**
 * Created by Marijan on 23.3.2018..
 */
interface IMainRepository {
    fun loadCategories(): Flowable<List<Category>>
    fun logout()
}