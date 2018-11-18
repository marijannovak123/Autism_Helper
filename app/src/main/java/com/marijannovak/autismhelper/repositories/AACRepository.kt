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
        private val sentenceDao: SavedSentenceDao,
        private val phraseCategoryDao: PhraseCategoryDao,
        @Named(Constants.SCHEDULER_IO) private val ioScheduler: Scheduler,
        @Named(Constants.SCHEDULER_MAIN) private val mainScheduler: Scheduler) {

    suspend fun getPhrases(): ReceiveChannel<PhrasesSavedSentencesJoin> {
        return aacSource.getPhraseSentencesJoinChannel()
    }

    fun getCategoryPhrases(phraseCategoryId: Int): Flowable<List<AacPhrase>> {
        return aacDao.getCategoryPhrases(phraseCategoryId)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }

    suspend fun savePhrase(phrase: AacPhrase): Completion {
        return Completion.create {
            aacSource.savePhrase(phrase)
        }
//        return Completable.fromAction {
//                aacDao.insert(phrase)
//        }.subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

    fun deletePhrase(phrase: AacPhrase): Completable {
        return Completable.fromAction {
            aacDao.delete(phrase)
        }.subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

    fun saveSentence(savedSentence: SavedSentence): Completable {
        return Completable.fromAction {
                sentenceDao.insert(savedSentence)
        }.subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

    fun loadPhraseCategories(): Flowable<List<PhraseCategory>> {
        return phraseCategoryDao.getPhraseCategories()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }

    fun loadSentences(): Flowable<List<SavedSentence>> {
        return sentenceDao.getSavedSentences()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }

    fun deleteSentence(sentence: SavedSentence): Completable {
        return Completable.fromAction {
            sentenceDao.delete(sentence)
        }.subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

}