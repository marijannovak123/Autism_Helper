package com.marijannovak.autismhelper.data.network.service

import com.marijannovak.autismhelper.config.Constants.Companion.RSS_URL
import com.marijannovak.autismhelper.data.models.RSS
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.CoroutineHelper
import javax.inject.Inject

class FeedService @Inject constructor(
        private val api: API
) {
    suspend fun getFeed(): RSS {
        return CoroutineHelper.awaitDeferred {
            api.getFeed(RSS_URL)
        }
    }
}