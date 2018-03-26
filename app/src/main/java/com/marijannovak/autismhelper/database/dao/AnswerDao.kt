package com.marijannovak.autismhelper.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_ANSWERS
import com.marijannovak.autismhelper.models.Answer
import com.marijannovak.autismhelper.models.Question
import io.reactivex.Flowable

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface AnswerDao {

    @Query("SELECT * FROM $TABLE_ANSWERS")
    fun getAnswers() : Flowable<List<Answer>>

    @Query("SELECT * FROM $TABLE_ANSWERS WHERE id = :id")
    fun getAnswerById(id : Int) : Flowable<Question>

    @Query("SELECT COUNT(*) FROM $TABLE_ANSWERS")
    fun getAnswerCount() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAnswer(answer: Answer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAnswers(answers : List<Answer>)

    @Delete
    fun delete(answer: Answer)

    @Delete
    fun deleteMultiple(answers : List<Answer>)
}