package com.marijannovak.autismhelper.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_QUESTION_TYPES
import com.marijannovak.autismhelper.models.Question
import com.marijannovak.autismhelper.models.QuestionType
import io.reactivex.Flowable

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface QuestionTypeDao {
    @Query("SELECT * FROM $TABLE_QUESTION_TYPES")
    fun getQuestionTypes() : Flowable<List<Question>>

    @Query("SELECT * FROM $TABLE_QUESTION_TYPES WHERE id = :id")
    fun getQuestionTypeById(id : Int) : Flowable<Question>

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
}