package com.marijannovak.autismhelper.repositories

import com.marijannovak.autismhelper.data.database.datasource.ChildDataSource
import com.marijannovak.autismhelper.data.models.Child
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Marijan on 23.3.2018..
 */
@Singleton
class MainRepository @Inject constructor(
        private val childSource: ChildDataSource
) {

    suspend fun getChildren(): ReceiveChannel<List<Child>> {
        return childSource.childrenChannel()
    }

}