package com.marijannovak.autismhelper.data.database.datasource

import com.marijannovak.autismhelper.data.database.dao.ChildDao
import com.marijannovak.autismhelper.data.database.dao.ChildScoreDao
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.ChildScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.reactive.openSubscription
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChildDataSource @Inject constructor(
        private val childDao: ChildDao,
        private val childScoreDao: ChildScoreDao
) {
    suspend fun childrenChannel(): ReceiveChannel<List<Child>> {
        return withContext(Dispatchers.IO) {
            childDao.getChildren().openSubscription()
        }
    }

    suspend fun insertScore(score: ChildScore) {
        return withContext(Dispatchers.IO) {
            async {
                childScoreDao.insert(score)
            }.await()
        }
    }

}