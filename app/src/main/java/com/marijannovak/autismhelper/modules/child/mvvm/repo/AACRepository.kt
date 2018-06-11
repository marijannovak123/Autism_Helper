package com.marijannovak.autismhelper.modules.child.mvvm.repo

import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.database.dao.AACDao
import com.marijannovak.autismhelper.data.database.dao.SavedSentenceDao
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.PhrasesSavedSentencesJoin
import com.marijannovak.autismhelper.data.models.SavedSentence
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import org.jetbrains.anko.doAsync
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AACRepository @Inject constructor(
        private val aacDao: AACDao,
        private val sentenceDao: SavedSentenceDao,
        @Named(Constants.SCHEDULER_IO) private val ioScheduler: Scheduler,
        @Named(Constants.SCHEDULER_MAIN) private val mainScheduler: Scheduler) {

    fun getPhrases(): Flowable<PhrasesSavedSentencesJoin> {
        return Flowable.zip(
                    aacDao.getAllPhrases(),
                    sentenceDao.getSavedSentences(),
                    BiFunction<List<AacPhrase>, List<SavedSentence>, PhrasesSavedSentencesJoin> {
                        phrases, sentences -> PhrasesSavedSentencesJoin(phrases, sentences)
                    }
                ).subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }

    fun savePhrase(phrase: AacPhrase): Completable {
        return Completable.fromAction {
                aacDao.insert(phrase)
        }.subscribeOn(ioScheduler).observeOn(mainScheduler)
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

}