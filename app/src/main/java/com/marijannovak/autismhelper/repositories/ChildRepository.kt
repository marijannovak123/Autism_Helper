package com.marijannovak.autismhelper.repositories

import com.marijannovak.autismhelper.data.database.datasource.CategoryDataSource
import com.marijannovak.autismhelper.data.models.Category
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChildRepository @Inject constructor(
        private val categorySource: CategoryDataSource
) {
    suspend fun loadCategories(): ReceiveChannel<List<Category>> {
        return categorySource.getCategoriesChannel()
    }
}