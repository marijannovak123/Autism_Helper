package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_QUESTION_TYPES
import com.marijannovak.autismhelper.data.models.QuestionType
import io.reactivex.Flowable

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface QuestionTypeDao: BaseDao<QuestionType> {
    @Query("SELECT * FROM $TABLE_QUESTION_TYPES")
    fun getQuestionTypes() : Flowable<List<QuestionType>>

    @Query("SELECT * FROM $TABLE_QUESTION_TYPES WHERE id = :id")
    fun getQuestionTypeById(id : Int) : Flowable<QuestionType>

    @Query("SELECT COUNT(*) FROM $TABLE_QUESTION_TYPES")
    fun getQuestionTypeCount() : Int

    @Query("DELETE FROM $TABLE_QUESTION_TYPES")
    fun deleteTable()
}