package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_QUESTIONS
import com.marijannovak.autismhelper.data.models.Question
import com.marijannovak.autismhelper.data.models.QuestionAnswersJoin
import io.reactivex.Flowable

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface QuestionDao: BaseDao<Question> {

    @Transaction
    @Query("SELECT * FROM $TABLE_QUESTIONS")
    fun getQuestions() : Flowable<List<QuestionAnswersJoin>>

    @Transaction
    @Query("SELECT * FROM $TABLE_QUESTIONS WHERE id = :id")
    fun getQuestionById(id : Int) : Flowable<QuestionAnswersJoin>

    @Query("SELECT COUNT(*) FROM $TABLE_QUESTIONS")
    fun getQuestionCount() : Int

    @Query("DELETE FROM $TABLE_QUESTIONS")
    fun deleteTable()
}