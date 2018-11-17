package com.marijannovak.autismhelper.data.network.service

import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.network.API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserService @Inject constructor(
        private val api: API
) {
    suspend fun getUserData(userId: String): User {
        return withContext(Dispatchers.IO) {
            api.getUser(userId).await()
        }
    }

    suspend fun uploadScores(userId: String, scoresToUpload: List<ChildScore>) {
        return withContext(Dispatchers.IO) {
            scoresToUpload.forEach {
                async { api.putScore(userId, it.id, it) }.await()
            }
        }
    }

    suspend fun uploadUser(user: User) {
        return withContext(Dispatchers.IO) {
            api.putUser(user.id, user).await()
        }
    }

}