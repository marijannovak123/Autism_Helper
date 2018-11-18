package com.marijannovak.autismhelper.data.database.datasource

import com.marijannovak.autismhelper.data.database.dao.CategoryDao
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.reactive.openSubscription
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryDataSource @Inject constructor(
        private val categoryDao: CategoryDao
) {
    suspend fun getCategoryWithQuestionsChannel(categoryId: Int): ReceiveChannel<CategoryQuestionsAnswersJoin> {
        return withContext(Dispatchers.IO) {
            categoryDao.getCategoryWithQuestions(categoryId).distinctUntilChanged().openSubscription()
        }
    }

    suspend fun getCategoriesChannel(): ReceiveChannel<List<Category>> {
        return withContext(Dispatchers.IO) {
            categoryDao.getCategories().distinctUntilChanged().openSubscription()
        }
    }

}