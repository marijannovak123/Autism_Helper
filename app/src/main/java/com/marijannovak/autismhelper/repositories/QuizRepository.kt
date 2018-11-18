package com.marijannovak.autismhelper.repositories

import com.marijannovak.autismhelper.data.database.datasource.CategoryDataSource
import com.marijannovak.autismhelper.data.database.datasource.ChildDataSource
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.network.service.ChildService
import com.marijannovak.autismhelper.utils.Completion
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
        private val categorySource: CategoryDataSource,
        private val childSource: ChildDataSource,
        private val childService: ChildService
) {

    suspend fun getCategoryData(categoryId: Int): ReceiveChannel<CategoryQuestionsAnswersJoin> {
        return categorySource.getCategoryWithQuestionsChannel(categoryId)
    }

    suspend fun saveScoreLocallyAndOnline(score: ChildScore): Completion {
        val scoreToSave = score.copy(id = Math.abs(score.hashCode()))
        try {
            childService.uploadScore(scoreToSave)
        } catch (e: Exception) {
            //don't throw exception, continue with inserting to db
        }

        return Completion.create {
            childSource.insertScore(scoreToSave)
        }
    }
}