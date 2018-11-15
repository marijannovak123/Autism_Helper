package com.marijannovak.autismhelper.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_ANSWERS
import com.marijannovak.autismhelper.data.models.Answer
import io.reactivex.Single

@Dao
interface AnswerDao : BaseDao<Answer> {

    @Query("SELECT * FROM $TABLE_ANSWERS")
    fun getAnswers(): Single<List<Answer>>

    @Query("DELETE FROM $TABLE_ANSWERS")
    fun deleteTable()
}