package com.marijannovak.autismhelper.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CHILD_SCORES
import com.marijannovak.autismhelper.data.models.ChildScore
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ChildScoreDao : BaseDao<ChildScore> {

    @Query("SELECT * FROM $TABLE_CHILD_SCORES WHERE childId = :childId")
    fun getChildScores(childId: String): Flowable<List<ChildScore>>

    @Query("SELECT * FROM $TABLE_CHILD_SCORES")
    fun queryAll(): List<ChildScore>

    @Query("SELECT * FROM $TABLE_CHILD_SCORES WHERE id = :id")
    fun getChildScoreById(id: Int): Flowable<ChildScore>

    @Query("SELECT COUNT(*) FROM $TABLE_CHILD_SCORES")
    fun getChildScoreCount(): Single<Int>

    @Query("DELETE FROM $TABLE_CHILD_SCORES")
    fun deleteTable()
}