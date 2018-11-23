package com.marijannovak.autismhelper.data.network.service

import com.marijannovak.autismhelper.config.Constants.Companion.RSS_URL
import com.marijannovak.autismhelper.data.models.RSS
import com.marijannovak.autismhelper.data.network.API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeedService @Inject constructor(
        private val api: API
) {
    suspend fun getFeed(): RSS {
        return withContext(Dispatchers.IO) {
            api.getFeed(RSS_URL).await()
        }
    }
}