package com.marijannovak.autismhelper.data.database.datasource

import com.marijannovak.autismhelper.data.database.dao.*
import com.marijannovak.autismhelper.data.models.ContentWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataSource @Inject constructor(
        private val aacDao: AACDao,
        private val phraseCategoryDao: PhraseCategoryDao,
        private val categoryDao: CategoryDao,
        private val questionDao: QuestionDao,
        private val answerDao: AnswerDao
) {
    suspend fun saveContent(contentWrapper: ContentWrapper, firstSync: Boolean) {
        return withContext(Dispatchers.IO) {
            async {
                with(contentWrapper) {
                    categoryDao.insertMultiple(categories)
                    phraseCategoryDao.insertMultiple(phraseCategories)

                    if(firstSync) {
                        questionDao.insertMultiple(questions)
                    } else {
                        questionDao.updateMultiple(questions)
                    }

                    questions.forEach {
                        answerDao.insertMultiple(it.answers)
                    }

                    if(firstSync) {
                        aacDao.insertMultiple(phrases)
                    } else {
                        aacDao.updateMultiple(phrases)
                    }
                }
            }.await()
        }
    }
}