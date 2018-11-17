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

            //       return Completable.mergeArray(
//                api.getCategories()
//                        .doOnSuccess {
//                            db.categoriesDao().insertMultiple(it)
//                        }.toCompletable(),
//                api.getQuestions()
//                        .doOnSuccess {
//                            if(firstSync) {
//                                db.questionDao().insertMultiple(it)
//                            } else {
//                                db.questionDao().updateMultiple(it)
//                            }
//                            for (question: Question in it) {
//                                db.answerDao().insertMultiple(question.answers)
//                                if (question.categoryId == 2) {
//                                    questionsWithImgs += question
//                                }
//                            }
//                        }.toCompletable(),
//                api.getPhrases()
//                        .doOnSuccess {
//                            phrases = it
//                            if(firstSync) {
//                                db.aacDao().insertMultiple(it)
//                            } else {
//                                db.aacDao().updateMultiple(it)
//                            }
//                        }.toCompletable(),
//                api.getPhraseCategories()
//                        .doOnSuccess{
//                            db.phraseCategoryDao().insertMultiple(it)
//                        }.toCompletable()
//        ).subscribeOn(ioScheduler)
//                .observeOn(mainScheduler)
        }
    }
}