package com.marijannovak.autismhelper.data.database.datasource

import android.util.Log
import com.marijannovak.autismhelper.data.database.dao.ChildDao
import com.marijannovak.autismhelper.data.database.dao.ChildScoreDao
import com.marijannovak.autismhelper.data.database.dao.UserDao
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.utils.PrefsHelper
import com.marijannovak.autismhelper.utils.logTag
import com.marijannovak.autismhelper.utils.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserDataSource @Inject constructor(
        private val userDao: UserDao,
        private val childDao: ChildDao,
        private val childScoreDao: ChildScoreDao,
        private val prefs: PrefsHelper
) {
    suspend fun getLoggedInUser(): User {
        return withContext(Dispatchers.IO) {
            async { userDao.getUserRaw() }.await()
        }
    }

    suspend fun updateUser(userData: User) {
        return withContext(Dispatchers.IO) {
            async {
                userDao.update(userData.username!!, userData.parentPassword!!)
                userData.children?.let {
                    childDao.updateMultiple(it.mapToList())
                }
                userData.childScores?.let {
                    childScoreDao.insertMultiple(it.mapToList())
                }
                prefs.setParentPassword(userData.parentPassword ?: "")
            }.await()
        }
    }

    suspend fun getScoresToUpload(scores: List<ChildScore>): List<ChildScore> {
        return withContext(Dispatchers.IO) {
            async {
                val scoresToUpload = ArrayList<ChildScore>()
                val childScores = childScoreDao.queryAll()
                childScores.forEach { if(!scores.contains(it)) scoresToUpload += it }
                scoresToUpload
            }.await()
        }
    }

    suspend fun saveUser(user: User) {
        return withContext(Dispatchers.IO) {
            async {
                userDao.insert(user)
                user.children?.let {
                    childDao.insertMultiple(it.mapToList())
                }
                user.childScores?.let {
                    childScoreDao.insertMultiple(it.mapToList())
                }
                prefs.setParentPassword(user.parentPassword ?: "")
            }.await()
        }
    }
}