package com.marijannovak.autismhelper.data.network.service

import com.marijannovak.autismhelper.data.models.ContentWrapper
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.CoroutineHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataService @Inject constructor (
        private val api: API
) {

    suspend fun getContent(): ContentWrapper {
        return CoroutineHelper.combineResult(
                api.getCategories(),
                api.getQuestions(),
                api.getPhrases(),
                api.getPhraseCategories()
        ) {
            categories, questions, phrases, phraseCategories ->
                ContentWrapper(categories, questions, phrases, phraseCategories)
        }

    }

}