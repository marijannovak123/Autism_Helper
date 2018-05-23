package com.marijannovak.autismhelper.modules.child.mvvm.repo

import com.marijannovak.autismhelper.data.database.dao.CategoryDao
import com.marijannovak.autismhelper.data.database.dao.ChildScoreDao
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.handleThreading
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class QuizRepository @Inject constructor(
        private val categoryDao: CategoryDao,
        private val childScoreDao: ChildScoreDao,
        private val api: API
) {

    fun getCategoryData(categoryId: Int): Flowable<CategoryQuestionsAnswersJoin> {
        return categoryDao
                .getCategoryWithQuestions(categoryId)
                .handleThreading()
    }

    fun saveScoreLocallyAndOnline(score: ChildScore): Completable {
        val scoreToSave = score.copy(id = Math.abs(score.hashCode()))
        return Completable.mergeArray(
                api.putScore(scoreToSave.parentId, scoreToSave.id, scoreToSave),
                Completable.fromAction {
                    childScoreDao.insert(scoreToSave)
                }
        ).handleThreading()
    }
}