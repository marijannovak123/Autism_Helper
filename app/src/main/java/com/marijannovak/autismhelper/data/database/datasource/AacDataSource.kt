package com.marijannovak.autismhelper.data.database.datasource

import com.marijannovak.autismhelper.data.database.dao.AACDao
import com.marijannovak.autismhelper.data.database.dao.SavedSentenceDao
import com.marijannovak.autismhelper.data.models.AacPhrase
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
        private val sentenceDao: SavedSentenceDao
) {

    suspend fun getAllPhrasesChannel(): ReceiveChannel<List<AacPhrase>> {
        return withContext(Dispatchers.IO) {
            aacDao.getAllPhrases().openSubscription()
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
}