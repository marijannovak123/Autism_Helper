package com.marijannovak.autismhelper.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.models.Question
import io.reactivex.Flowable

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions")
    fun getQuestions() : Flowable<List<Question>>

    @Query("SELECT * FROM questions WHERE id = :id")
    fun getQuestionById(id : Int) : Flowable<Question>

    @Query("SELECT COUNT(*) FROM questions")
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
}