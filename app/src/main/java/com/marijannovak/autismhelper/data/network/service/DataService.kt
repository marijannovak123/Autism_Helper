package com.marijannovak.autismhelper.data.network.service

import com.marijannovak.autismhelper.data.models.*
import com.marijannovak.autismhelper.data.network.API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataService @Inject constructor (
        private val api: API
) {

    suspend fun getContent(): ContentWrapper {
        return withContext(Dispatchers.IO) {
            val categories = api.getCategories().await()
            val questions = api.getQuestions().await()
            val phrases = api.getPhrases().await()
            val phraseCategories = api.getPhraseCategories().await()
            ContentWrapper(categories, questions, phrases, phraseCategories)
        }
    }

}