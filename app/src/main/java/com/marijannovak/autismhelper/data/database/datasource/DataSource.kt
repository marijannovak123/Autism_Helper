package com.marijannovak.autismhelper.data.database.datasource

import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.data.database.dao.*
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.ContentWrapper
import com.marijannovak.autismhelper.data.models.Question
import com.marijannovak.autismhelper.utils.CoroutineHelper
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class DataSource @Inject constructor(
        private val aacDao: AACDao,
        private val phraseCategoryDao: PhraseCategoryDao,
        private val categoryDao: CategoryDao,
        private val questionDao: QuestionDao,
        private val answerDao: AnswerDao,
        private val database: AppDatabase
) {
    suspend fun saveContent(contentWrapper: ContentWrapper, firstSync: Boolean) {
        return CoroutineHelper.deferredCall {
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
        }
    }

    fun updateQuestionImgPath(question: Question, path: String) {
        doAsync {
            question.imgPath = path
            questionDao.insert(question)
        }
    }

    suspend fun clearDb() {
        return CoroutineHelper.deferredCall {
            database.clearAllTables()
        }
    }

    suspend fun insertPhrase(phrase: AacPhrase) {
        return CoroutineHelper.deferredCall {
            aacDao.insert(phrase)
        }
    }

    fun updatePhraseImgPath(phrase: AacPhrase, path: String) {
        phrase.iconPath = path
        aacDao.insert(phrase)
    }
}