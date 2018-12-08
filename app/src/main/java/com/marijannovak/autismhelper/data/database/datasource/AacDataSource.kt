package com.marijannovak.autismhelper.data.database.datasource

import com.marijannovak.autismhelper.data.database.dao.AACDao
import com.marijannovak.autismhelper.data.database.dao.PhraseCategoryDao
import com.marijannovak.autismhelper.data.database.dao.SavedSentenceDao
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.PhraseCategory
import com.marijannovak.autismhelper.data.models.PhrasesSavedSentencesJoin
import com.marijannovak.autismhelper.data.models.SavedSentence
import com.marijannovak.autismhelper.utils.CoroutineHelper
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject

class AacDataSource @Inject constructor(
        private val aacDao: AACDao,
        private val sentenceDao: SavedSentenceDao,
        private val phraseCategoryDao: PhraseCategoryDao
) {

    suspend fun getAllPhrasesChannel(): ReceiveChannel<List<AacPhrase>> {
        return CoroutineHelper.openFlowableChannel {
            aacDao.getAllPhrases()
        }
    }

    suspend fun getPhraseSentencesJoinChannel(): ReceiveChannel<PhrasesSavedSentencesJoin> {
        return CoroutineHelper.openFlowableChannel {
            Flowable.zip(
                    aacDao.getAllPhrases(),
                    sentenceDao.getSavedSentences(),
                    BiFunction<List<AacPhrase>, List<SavedSentence>, PhrasesSavedSentencesJoin> { phrases, sentences ->
                        PhrasesSavedSentencesJoin(phrases, sentences)
                    }
            )
        }
    }

    suspend fun savePhrase(phrase: AacPhrase) {
        return CoroutineHelper.deferredCall {
            aacDao.insert(phrase)
        }
    }

    suspend fun getCategoryPhrasesChannel(phraseCategoryId: Int): ReceiveChannel<List<AacPhrase>> {
        return CoroutineHelper.openFlowableChannel {
            aacDao.getCategoryPhrases(phraseCategoryId)
        }
    }

    suspend fun saveSentence(savedSentence: SavedSentence) {
        return CoroutineHelper.deferredCall {
            sentenceDao.insert(savedSentence)
        }
    }

    suspend fun getPhraseCategoriesChannel(): ReceiveChannel<List<PhraseCategory>> {
        return CoroutineHelper.openFlowableChannel {
            phraseCategoryDao.getPhraseCategories()
        }
    }

    suspend fun getSavedSentences(): ReceiveChannel<List<SavedSentence>> {
        return CoroutineHelper.openFlowableChannel {
            sentenceDao.getSavedSentences()
        }
    }

    suspend fun deleteSentence(sentence: SavedSentence) {
        return CoroutineHelper.deferredCall {
            sentenceDao.delete(sentence)
        }
    }

    suspend fun deletePhrase(phrase: AacPhrase) {
        return CoroutineHelper.deferredCall {
            aacDao.delete(phrase)
        }
    }

    suspend fun getPhrasesSuspend(): List<AacPhrase> {
        return aacDao.getPhrasesSuspend()
    }
}