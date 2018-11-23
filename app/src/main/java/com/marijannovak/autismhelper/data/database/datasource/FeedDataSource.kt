package com.marijannovak.autismhelper.data.database.datasource

import com.marijannovak.autismhelper.data.database.dao.FeedItemDao
import com.marijannovak.autismhelper.data.models.FeedItem
import com.marijannovak.autismhelper.data.models.RSS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeedDataSource @Inject constructor(
        private val feedItemDao: FeedItemDao
) {
    suspend fun getItems(): List<FeedItem> {
        return withContext(Dispatchers.IO) {
            async {
                feedItemDao.getItems()
            }.await()
        }
    }

    suspend fun saveRss(feed: RSS) {
        return withContext(Dispatchers.IO) {
            async {
                feedItemDao.insertMultiple(feed.channel.feedItems)
            }.await()
        }
    }

}