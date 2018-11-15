package com.marijannovak.autismhelper.common.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultiple(modelList: List<T>)

    @Delete
    fun delete(model: T)

    @Delete
    fun deleteMultiple(modelList: List<T>)
}