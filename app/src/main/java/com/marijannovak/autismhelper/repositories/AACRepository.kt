package com.marijannovak.autismhelper.repositories

import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.database.dao.AACDao
import com.marijannovak.autismhelper.data.database.dao.PhraseCategoryDao
import com.marijannovak.autismhelper.data.database.dao.SavedSentenceDao
import com.marijannovak.autismhelper.data.database.datasource.AacDataSource
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.PhraseCategory
import com.marijannovak.autismhelper.data.models.PhrasesSavedSentencesJoin
import com.marijannovak.autismhelper.data.models.SavedSentence
import com.marijannovak.autismhelper.utils.Completion
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AACRepository @Inject constructor(
        private val aacSource: AacDataSource,
        private val aacDao: AACDao,
        @Named(Constants.SCHEDULER_IO) private val ioScheduler: Scheduler,
        @Named(Constants.SCHEDULER_MAIN) private val mainScheduler: Scheduler) {

    suspend fun getPhrases(): ReceiveChannel<PhrasesSavedSentencesJoin> {
        return aacSource.getPhraseSentencesJoinChannel()
    }

    suspend fun getCategoryPhrases(phraseCategoryId: Int): ReceiveChannel<List<AacPhrase>> {
        return aacSource.getCategoryPhrasesChannel(phraseCategoryId)
    }

    suspend fun savePhrase(phrase: AacPhrase): Completion {
        return Completion.create {
            aacSource.savePhrase(phrase)
        }
    }

    suspend fun deletePhrase(phrase: AacPhrase): Completion {
        return Completion.create {
            aacSource.deletePhrase(phrase)
        }
    }

    suspend fun saveSentence(savedSentence: SavedSentence): Completion {
        return Completion.create {
            aacSource.saveSentence(savedSentence)
        }
    }

    suspend fun loadPhraseCategories(): ReceiveChannel<List<PhraseCategory>> {
        return aacSource.getPhraseCategoriesChannel()
    }

    fun loadSentences(): ReceiveChannel<List<SavedSentence>> {
        return aacSource.getSavedSentences()
    }

    suspend fun deleteSentence(sentence: SavedSentence): Completion {
        return Completion.create {
            aacSource.deleteSentence(sentence)
        }
    }

}