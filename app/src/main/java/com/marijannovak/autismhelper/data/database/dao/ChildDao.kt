package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CHILDREN
import com.marijannovak.autismhelper.data.models.Child
import io.reactivex.Single

@Dao
interface ChildDao: BaseDao<Child> {

    @Query("SELECT * FROM $TABLE_CHILDREN")
    fun getChildren(): Single<List<Child>>

    @Query("DELETE FROM $TABLE_CHILDREN")
    fun deleteTable()
}