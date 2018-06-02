package com.marijannovak.autismhelper.modules.child.mvvm.repo

import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.database.dao.CategoryDao
import com.marijannovak.autismhelper.data.database.dao.ChildScoreDao
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.network.API
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
        private val categoryDao: CategoryDao,
        private val childScoreDao: ChildScoreDao,
        @Named(Constants.API_JSON)
        private val api: API,
        @Named(Constants.SCHEDULER_IO) private val ioScheduler: Scheduler,
        @Named(Constants.SCHEDULER_MAIN) private val mainScheduler: Scheduler
) {

    fun getCategoryData(categoryId: Int): Flowable<CategoryQuestionsAnswersJoin> {
        return categoryDao
                .getCategoryWithQuestions(categoryId)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }

    fun saveScoreLocallyAndOnline(score: ChildScore): Completable {
        val scoreToSave = score.copy(id = Math.abs(score.hashCode()))
        return api.putScore(scoreToSave.parentId, scoreToSave.id, scoreToSave)
                .onErrorComplete()
                .doOnComplete {
                    childScoreDao.insert(scoreToSave)
                }.subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }
}