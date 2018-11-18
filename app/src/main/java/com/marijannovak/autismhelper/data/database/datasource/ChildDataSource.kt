package com.marijannovak.autismhelper.data.database.datasource

import com.marijannovak.autismhelper.data.database.dao.ChildDao
import com.marijannovak.autismhelper.data.models.Child
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.reactive.openSubscription
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChildDataSource @Inject constructor(
        private val childDao: ChildDao
) {
    suspend fun childrenChannel(): ReceiveChannel<List<Child>> {
        return withContext(Dispatchers.IO) {
            childDao.getChildren().openSubscription()
        }
    }

}