package com.marijannovak.autismhelper.data.database.datasource

import com.marijannovak.autismhelper.data.database.dao.AACDao
import com.marijannovak.autismhelper.data.database.dao.PhraseCategoryDao
import com.marijannovak.autismhelper.data.database.dao.SavedSentenceDao
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.PhraseCategory
import com.marijannovak.autismhelper.data.models.PhrasesSavedSentencesJoin
import com.marijannovak.autismhelper.data.models.SavedSentence
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.reactive.openSubscription
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AacDataSource @Inject constructor(
        private val aacDao: AACDao,
        private val sentenceDao: SavedSentenceDao,
        private val phraseCategoryDao: PhraseCategoryDao
) {

    suspend fun getAllPhrasesChannel(): ReceiveChannel<List<AacPhrase>> {
        return withContext(Dispatchers.IO) {
            aacDao.getAllPhrases().distinctUntilChanged().openSubscription()
        }
    }

    suspend fun getPhraseSentencesJoinChannel(): ReceiveChannel<PhrasesSavedSentencesJoin> {
        return withContext(Dispatchers.IO) {
            Flowable.zip(
                    aacDao.getAllPhrases(),
                    sentenceDao.getSavedSentences(),
                    BiFunction<List<AacPhrase>, List<SavedSentence>, PhrasesSavedSentencesJoin> {
                        phrases, sentences -> PhrasesSavedSentencesJoin(phrases, sentences)
                    }
            ).openSubscription()
        }
    }

    suspend fun savePhrase(phrase: AacPhrase) {
        return withContext(Dispatchers.IO) {
            async {
                aacDao.insert(phrase)
            }.await()
        }
    }

    suspend fun getCategoryPhrasesChannel(phraseCategoryId: Int): ReceiveChannel<List<AacPhrase>> {
        return withContext(Dispatchers.IO) {
            aacDao.getCategoryPhrases(phraseCategoryId).distinctUntilChanged().openSubscription()
        }
    }

    suspend fun saveSentence(savedSentence: SavedSentence) {
        return withContext(Dispatchers.IO) {
            async {
                sentenceDao.insert(savedSentence)
            }.await()
        }
    }

    suspend fun getPhraseCategoriesChannel(): ReceiveChannel<List<PhraseCategory>> {
        return withContext(Dispatchers.IO) {
            phraseCategoryDao.getPhraseCategories().distinctUntilChanged().openSubscription()
        }
    }

    fun getSavedSentences(): ReceiveChannel<List<SavedSentence>> {
        return sentenceDao.getSavedSentences().distinctUntilChanged().openSubscription()
    }

    suspend fun deleteSentence(sentence: SavedSentence) {
        return withContext(Dispatchers.IO) {
            async {
                sentenceDao.delete(sentence)
            }.await()
        }
    }

    suspend fun deletePhrase(phrase: AacPhrase) {
        return withContext(Dispatchers.IO) {
            async {
                aacDao.delete(phrase)
            }.await()
        }
    }
}