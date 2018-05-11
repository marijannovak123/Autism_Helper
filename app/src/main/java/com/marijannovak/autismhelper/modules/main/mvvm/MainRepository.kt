package com.marijannovak.autismhelper.modules.main.mvvm

import com.marijannovak.autismhelper.data.database.dao.ChildDao
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.utils.handleThreading
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Created by Marijan on 23.3.2018..
 */
class MainRepository @Inject constructor(private val childDao: ChildDao) {

    fun getChildren(): Flowable<List<Child>> {
        return childDao
                .getChildren()
                .handleThreading()
    }

}