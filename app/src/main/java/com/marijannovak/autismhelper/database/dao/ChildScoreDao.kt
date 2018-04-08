package com.marijannovak.autismhelper.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CHILD_SCORES
import com.marijannovak.autismhelper.models.ChildScore
import io.reactivex.Flowable

@Dao
interface ChildScoreDao {

    @Query("SELECT * FROM $TABLE_CHILD_SCORES")
    fun getChildScores() : Flowable<List<ChildScore>>

    @Query("SELECT * FROM $TABLE_CHILD_SCORES WHERE id = :id")
    fun getChildScoreById(id : Int) : Flowable<ChildScore>

    @Query("SELECT COUNT(*) FROM $TABLE_CHILD_SCORES")
    fun getChildScoreCount() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveChildScore(childScore : ChildScore)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveChildScores(childScores : List<ChildScore>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateChildScore(childScore : ChildScore)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateChildScores(childScores : List<ChildScore>)

    @Delete
    fun delete(childScore : ChildScore)

    @Delete
    fun deleteMultiple(childScores : List<ChildScore>)

    @Query("DELETE FROM $TABLE_CHILD_SCORES")
    fun deleteTable()
}