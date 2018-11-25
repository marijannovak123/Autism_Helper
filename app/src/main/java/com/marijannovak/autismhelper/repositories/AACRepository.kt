package com.marijannovak.autismhelper.repositories

import com.marijannovak.autismhelper.data.database.datasource.AacDataSource
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.PhraseCategory
import com.marijannovak.autismhelper.data.models.PhrasesSavedSentencesJoin
import com.marijannovak.autismhelper.data.models.SavedSentence
import com.marijannovak.autismhelper.utils.Completion
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AACRepository @Inject constructor(
        private val aacSource: AacDataSource
) {

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

    suspend fun loadSentences(): ReceiveChannel<List<SavedSentence>> {
        return aacSource.getSavedSentences()
    }

    suspend fun deleteSentence(sentence: SavedSentence): Completion {
        return Completion.create {
            aacSource.deleteSentence(sentence)
        }
    }

}