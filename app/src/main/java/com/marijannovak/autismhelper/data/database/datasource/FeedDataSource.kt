package com.marijannovak.autismhelper.data.database.datasource

import com.marijannovak.autismhelper.data.database.dao.FeedItemDao
import com.marijannovak.autismhelper.data.models.FeedItem
import com.marijannovak.autismhelper.data.models.RSS
import com.marijannovak.autismhelper.utils.CoroutineHelper
import javax.inject.Inject

class FeedDataSource @Inject constructor(
        private val feedItemDao: FeedItemDao
) {
    suspend fun getItems(): List<FeedItem> {
        return feedItemDao.getItems()
    }

    suspend fun saveRss(feed: RSS) {
        return CoroutineHelper.deferredCall {
            with(feed.channel) {
                if(feedItems.isNotEmpty()) {
                    feedItemDao.insertMultiple(feed.channel.feedItems)
                }
            }
        }
    }

}