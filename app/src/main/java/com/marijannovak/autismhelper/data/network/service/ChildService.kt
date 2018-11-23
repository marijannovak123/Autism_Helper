package com.marijannovak.autismhelper.data.network.service

import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.Completion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChildService @Inject constructor(
        private val api: API
) {
    suspend fun uploadScore(scoreToSave: ChildScore) {
        return withContext(Dispatchers.IO) {
            with(scoreToSave) {
                api.putScore(parentId, id, scoreToSave).await()
            }
        }
    }

    suspend fun addChild(child: Child) {
        return withContext(Dispatchers.IO) {
            with(child) {
                api.addChild(parentId, id, child).await()
            }
        }
    }

    suspend fun deleteChild(child: Child) {
        return withContext(Dispatchers.IO) {
            with(child) {
                api.deleteChild(parentId, id).await()
            }
        }
    }

    suspend fun updateChild(child: Child) {
        return withContext(Dispatchers.IO) {
            api.updateChild(child.parentId, child.id, child).await()
        }
    }

}