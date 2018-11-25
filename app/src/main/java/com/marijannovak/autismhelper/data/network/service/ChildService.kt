package com.marijannovak.autismhelper.data.network.service

import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.Completion
import com.marijannovak.autismhelper.utils.CoroutineHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChildService @Inject constructor(
        private val api: API
) {
    suspend fun uploadScore(scoreToSave: ChildScore) {
        return CoroutineHelper.awaitDeferred {
            with(scoreToSave) {
                api.putScore(parentId, id, scoreToSave)
            }
        }
    }

    suspend fun addChild(child: Child) {
        return CoroutineHelper.awaitDeferred {
            with(child) {
                api.addChild(parentId, id, child)
            }
        }
    }

    suspend fun deleteChild(child: Child) {
        return CoroutineHelper.awaitDeferred {
            with(child) {
                api.deleteChild(parentId, id)
            }
        }
    }

    suspend fun updateChild(child: Child) {
        return CoroutineHelper.awaitDeferred {
            api.updateChild(child.parentId, child.id, child)
        }
    }

}