package com.marijannovak.autismhelper.common.base

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

interface BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(model : T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMultiple(modelList : List<T>)

    @Delete
    fun delete(model: T)

    @Delete
    fun deleteMultiple(modelList : List<T>)
}