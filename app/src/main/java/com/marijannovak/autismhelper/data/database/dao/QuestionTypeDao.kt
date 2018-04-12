package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_QUESTION_TYPES
import com.marijannovak.autismhelper.data.models.QuestionType
import io.reactivex.Flowable

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface QuestionTypeDao {
    @Query("SELECT * FROM $TABLE_QUESTION_TYPES")
    fun getQuestionTypes() : Flowable<List<QuestionType>>

    @Query("SELECT * FROM $TABLE_QUESTION_TYPES WHERE id = :id")
    fun getQuestionTypeById(id : Int) : Flowable<QuestionType>

    @Query("SELECT COUNT(*) FROM $TABLE_QUESTION_TYPES")
    fun getQuestionTypeCount() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveQuestionType(questionType : QuestionType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveQuestionTypes(questionTypes : List<QuestionType>)

    @Delete
    fun delete(questionType : QuestionType)

    @Delete
    fun deleteMultiple(questionTypes : List<QuestionType>)

    @Query("DELETE FROM $TABLE_QUESTION_TYPES")
    fun deleteTable()
}