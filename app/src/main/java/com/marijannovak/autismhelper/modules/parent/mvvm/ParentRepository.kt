package com.marijannovak.autismhelper.modules.parent.mvvm

import com.marijannovak.autismhelper.data.database.dao.ChildDao
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.handleThreading
import io.reactivex.Completable
import javax.inject.Inject

class ParentRepository @Inject constructor(private val childDao: ChildDao, private val api: API){
    fun saveChildLocallyAndOnline(child: Child): Completable {
        return Completable.mergeArray(
                api.addChild(child.parentId, child.id, child),
                Completable.fromAction {
                    childDao.insert(child)
                }
        ).handleThreading()
    }
}