package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_ANSWERS
import com.marijannovak.autismhelper.data.models.Answer
import io.reactivex.Single

@Dao
interface AnswerDao: BaseDao<Answer> {

    @Query("SELECT * FROM $TABLE_ANSWERS")
    fun getAnswers(): Single<List<Answer>>

    @Query("DELETE FROM $TABLE_ANSWERS")
    fun deleteTable()
}