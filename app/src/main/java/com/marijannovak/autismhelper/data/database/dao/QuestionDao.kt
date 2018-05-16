package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import android.support.v4.app.SupportActivity
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_QUESTIONS
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.Question
import com.marijannovak.autismhelper.data.models.QuestionAnswersJoin
import io.reactivex.Flowable

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface QuestionDao : BaseDao<Question> {

    @Transaction
    @Query("SELECT * FROM $TABLE_QUESTIONS")
    fun getQuestions(): Flowable<List<QuestionAnswersJoin>>

    @Transaction
    @Query("SELECT * FROM $TABLE_QUESTIONS WHERE id = :id")
    fun getQuestionById(id: Int): Flowable<QuestionAnswersJoin>

    @Query("SELECT COUNT(*) FROM $TABLE_QUESTIONS")
    fun getQuestionCount(): Int

    @Query("DELETE FROM $TABLE_QUESTIONS")
    fun deleteTable()

    @Query("UPDATE $TABLE_QUESTIONS  SET text = :text, categoryId = :categoryId, extraData = :extraData, imgPath = :imgPath WHERE id = :id")
    fun update(id: Int, text: String, categoryId: Int, extraData: String, imgPath: String)

    @Query("SELECT * FROM $TABLE_QUESTIONS")
    fun queryAll(): List<Question>

    @Transaction
    fun updateMultiple(questions: List<Question>){
        val savedQuestions = queryAll()
        var i = 0
        questions.forEach {
            if(i < savedQuestions.size && savedQuestions.contains(it)) {
                update(it.id, it.text, it.categoryId, it.extraData?: "", savedQuestions[i].imgPath ?: "")
            } else {
                insert(it)
            }
            i = i.inc()
        }
    }
}