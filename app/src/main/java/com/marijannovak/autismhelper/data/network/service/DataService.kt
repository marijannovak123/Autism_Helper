package com.marijannovak.autismhelper.data.network.service

import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.data.models.PhraseCategory
import com.marijannovak.autismhelper.data.models.Question
import com.marijannovak.autismhelper.data.network.API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataService @Inject constructor (
        private val api: API
) {
    suspend fun getCategories(): List<Category> {
        return withContext(Dispatchers.IO) {
            api.getCategories().await()
        }
    }

    suspend fun getQuestions(): List<Question> {
        return withContext(Dispatchers.IO) {
            api.getQuestions().await()
        }
    }

    suspend fun getPhrases(): List<AacPhrase> {
        return withContext(Dispatchers.IO) {
            api.getPhrases().await()
        }
    }

    suspend fun getPhraseCategories(): List<PhraseCategory> {
        return withContext(Dispatchers.IO) {
            api.getPhraseCategories().await()
        }
    }
}