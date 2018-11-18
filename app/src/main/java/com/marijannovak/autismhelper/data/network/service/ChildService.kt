package com.marijannovak.autismhelper.data.network.service

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

}