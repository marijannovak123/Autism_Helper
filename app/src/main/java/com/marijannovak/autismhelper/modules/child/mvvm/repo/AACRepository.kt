package com.marijannovak.autismhelper.modules.child.mvvm.repo

import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.database.dao.AACDao
import com.marijannovak.autismhelper.data.models.AacPhrase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import org.jetbrains.anko.doAsync
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AACRepository @Inject constructor(
        private val aacDao: AACDao,
        @Named(Constants.SCHEDULER_IO) private val ioScheduler: Scheduler,
        @Named(Constants.SCHEDULER_MAIN) private val mainScheduler: Scheduler) {

    fun getPhrases(): Flowable<List<AacPhrase>> {
        return aacDao
                .getAllPhrases()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }

    fun savePhrase(phrase: AacPhrase): Completable {
        return Completable.fromAction {
            doAsync {
                aacDao.insert(phrase)
            }
        }
    }

    fun deletePhrase(phrase: AacPhrase): Completable {
        return Completable.fromAction {
            doAsync {
                aacDao.delete(phrase)
            }
        }
    }

}