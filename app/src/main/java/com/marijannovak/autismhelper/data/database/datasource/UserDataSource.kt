package com.marijannovak.autismhelper.data.database.datasource

import com.marijannovak.autismhelper.data.database.dao.ChildDao
import com.marijannovak.autismhelper.data.database.dao.ChildScoreDao
import com.marijannovak.autismhelper.data.database.dao.UserDao
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.utils.CoroutineHelper
import com.marijannovak.autismhelper.utils.PrefsHelper
import com.marijannovak.autismhelper.utils.mapToList
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject

class UserDataSource @Inject constructor(
        private val userDao: UserDao,
        private val childDao: ChildDao,
        private val childScoreDao: ChildScoreDao,
        private val prefs: PrefsHelper
) {
    suspend fun getLoggedInUser(): User {
        return CoroutineHelper.deferredCall {
            userDao.getUserRaw()
        }
    }

    suspend fun updateUser(userData: User) {
        return CoroutineHelper.deferredCall {
            userDao.update(userData.username!!, userData.parentPassword!!)
            userData.children?.let {
                childDao.updateMultiple(it.mapToList())
            }
            userData.childScores?.let {
                childScoreDao.insertMultiple(it.mapToList())
            }
            prefs.setParentPassword(userData.parentPassword ?: "")
        }
    }

    suspend fun getScoresToUpload(scores: List<ChildScore>): List<ChildScore> {
        return CoroutineHelper.deferredCall {
            val scoresToUpload = ArrayList<ChildScore>()
            val childScores = childScoreDao.queryAll()
            childScores.forEach { if(!scores.contains(it)) scoresToUpload += it }
            scoresToUpload
        }
    }

    suspend fun saveUser(user: User) {
        return CoroutineHelper.deferredCall {
            userDao.insert(user)
            user.children?.let {
                childDao.insertMultiple(it.mapToList())
            }
            user.childScores?.let {
                childScoreDao.insertMultiple(it.mapToList())
            }
            prefs.setParentPassword(user.parentPassword ?: "")
        }
    }

    suspend fun getCurrentUserName(): String {
        return CoroutineHelper.deferredCall {
            userDao.getCurrentUser()?.username ?: "Parent"
        }
    }

    suspend fun getCurrentUser(): User {
        return CoroutineHelper.deferredCall {
            userDao.getCurrentUser()
        }
    }

    suspend fun updateAll(username: String, parentPassword: String, profilePicPath: String) {
        return CoroutineHelper.deferredCall {
            userDao.updateAll(username, parentPassword, profilePicPath)
        }
    }

    suspend fun getUserWithChildrenChannel(): ReceiveChannel<UserChildrenJoin> {
        return CoroutineHelper.openFlowableChannel{
            userDao.getUserWithChildren()
        }
    }

    fun insertUserNonSuspending(user: User) {
        userDao.insert(user)
    }

    suspend fun insertUserSuspending(user: User) {
        return CoroutineHelper.deferredCall {
            userDao.insert(user)
        }
    }

    fun getCurrentUserRaw(): User {
        return userDao.getCurrentUser()
    }

}