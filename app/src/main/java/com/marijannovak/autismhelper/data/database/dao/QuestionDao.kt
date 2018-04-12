package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_QUESTIONS
import com.marijannovak.autismhelper.data.models.Question
import io.reactivex.Flowable

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface QuestionDao {

    @Query("SELECT * FROM $TABLE_QUESTIONS")
    fun getQuestions() : Flowable<List<Question>>

    @Query("SELECT * FROM $TABLE_QUESTIONS WHERE id = :id")
    fun getQuestionById(id : Int) : Flowable<Question>

    @Query("SELECT COUNT(*) FROM $TABLE_QUESTIONS")
    fun getQuestionCount() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveQuestion(question : Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveQuestions(questions : List<Question>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateQuestion(question : Question)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateQuestions(questions : List<Question>)

    @Delete
    fun delete(question : Question)

    @Delete
    fun deleteMultiple(questions : List<Question>)

    @Query("DELETE FROM $TABLE_QUESTIONS")
    fun deleteTable()
}