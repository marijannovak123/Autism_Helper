package com.marijannovak.autismhelper.modules.child.mvvm

import com.marijannovak.autismhelper.data.database.dao.CategoryDao
import com.marijannovak.autismhelper.data.database.dao.ChildScoreDao
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.handleThreading
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

class QuizRepository @Inject constructor(
        private val categoryDao: CategoryDao,
        private val childScoreDao: ChildScoreDao,
        private val api: API
) {

    fun getCategoryData(categoryId: Int): Single<CategoryQuestionsAnswersJoin> {
        return categoryDao
                .getCategoryWithQuestions(categoryId)
                .handleThreading()
    }

    fun saveScoreLocallyAndOnline(score: ChildScore): Completable {
        val scoreToSave = score.copy(id = score.hashCode())
        return api.putScore(score.parentId, score.id, score)
                .andThen {
                    Completable.fromAction {
                        childScoreDao.insert(scoreToSave)
                    }
                }.handleThreading()
    }
}