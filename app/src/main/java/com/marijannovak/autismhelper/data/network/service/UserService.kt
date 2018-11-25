package com.marijannovak.autismhelper.data.network.service

import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.models.ParentPasswordRequest
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.models.UserUpdateRequest
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.CoroutineHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserService @Inject constructor(
        private val api: API
) {
    suspend fun getUserData(userId: String): User? {
        return CoroutineHelper.awaitDeferredResponse {
            api.getUser(userId)
        }
    }

    suspend fun uploadScores(userId: String, scoresToUpload: List<ChildScore>) {
        return withContext(Dispatchers.IO) {
            scoresToUpload.forEach {
                api.putScore(userId, it.id, it).await()
            }
        }
    }

    suspend fun uploadUser(user: User) {
        return CoroutineHelper.awaitDeferred {
            api.putUser(user.id, user)
        }
    }

    suspend fun updateUser(userId: String, userUpdateRequest: UserUpdateRequest) {
        return CoroutineHelper.awaitDeferred {
            api.updateParent(userId, userUpdateRequest)
        }
    }

    suspend fun updateParentPassword(id: String, parentPasswordRequest: ParentPasswordRequest) {
        return CoroutineHelper.awaitDeferred {
            api.updateParentPassword(id, parentPasswordRequest)
        }
    }

}