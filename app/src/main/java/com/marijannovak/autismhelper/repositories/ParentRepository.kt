package com.marijannovak.autismhelper.repositories

import com.marijannovak.autismhelper.data.database.datasource.AacDataSource
import com.marijannovak.autismhelper.data.database.datasource.ChildDataSource
import com.marijannovak.autismhelper.data.database.datasource.FeedDataSource
import com.marijannovak.autismhelper.data.database.datasource.UserDataSource
import com.marijannovak.autismhelper.data.models.*
import com.marijannovak.autismhelper.data.network.service.ChildService
import com.marijannovak.autismhelper.data.network.service.FeedService
import com.marijannovak.autismhelper.data.network.service.UserService
import com.marijannovak.autismhelper.utils.Completion
import com.marijannovak.autismhelper.utils.LoadResult
import com.marijannovak.autismhelper.utils.PrefsHelper
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParentRepository @Inject constructor(
        private val userSource: UserDataSource,
        private val userService: UserService,
        private val childService: ChildService,
        private val childDataSource: ChildDataSource,
        private val feedService: FeedService,
        private val feedDataSource: FeedDataSource,
        private val prefsHelper: PrefsHelper,
        private val aacSource: AacDataSource
) {
    suspend fun saveChildLocallyAndOnline(child: Child): Completion {
        return Completion.create {
            childService.addChild(child)
            childDataSource.insertChild(child)
        }
    }

    suspend fun loadUserWithChildren(): ReceiveChannel<UserChildrenJoin> {
        return userSource.getUserWithChildrenChannel()
    }

    suspend fun loadChildScoresLineData(child: Child): ReceiveChannel<ChartData> {
        return childDataSource.getScoresLineDataChannel(child)
    }

    suspend fun loadPhrases(): LoadResult<List<AacPhrase>> {
        return LoadResult.create {
            aacSource.getPhrasesSuspend()
        }
    }

    suspend fun updateUser(userId: String, userUpdateRequest: UserUpdateRequest, profilePicPath: String): Completion {
        return Completion.create {
            userService.updateUser(userId, userUpdateRequest)
            userSource.updateAll(userUpdateRequest.username, userUpdateRequest.parentPassword, profilePicPath)
            prefsHelper.setParentPassword(userUpdateRequest.parentPassword)
        }
    }

    suspend fun loadUser(): LoadResult<User> {
        return LoadResult.create {
            userSource.getCurrentUser()
        }
    }

    suspend fun loadUserName(): LoadResult<String> {
        return LoadResult.create {
            userSource.getCurrentUserName()
        }
    }

    fun getParentPassword() : String {
        return prefsHelper.getParentPassword()
    }

    suspend fun deleteChild(child: Child): Completion {
        return Completion.create {
            childService.deleteChild(child)
            childDataSource.deleteChild(child)
        }
    }

    suspend fun updateChild(child: Child): Completion {
        return Completion.create {
            childService.updateChild(child)
            childDataSource.updateChild(child)
        }
    }

    suspend fun fetchFeeds(): LoadResult<List<FeedItem>> {
        return LoadResult.refreshAndLoadLocalData(
                refreshBlock = {
                    val feed = feedService.getFeed()
                    feedDataSource.saveRss(feed)
                }, loadBlock = {
                    feedDataSource.getItems()
                }
        )
    }
}

