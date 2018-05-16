package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CHILDREN
import com.marijannovak.autismhelper.data.models.Child
import io.reactivex.Flowable

@Dao
interface ChildDao : BaseDao<Child> {

    @Query("SELECT * FROM $TABLE_CHILDREN")
    fun getChildren(): Flowable<List<Child>>

    @Query("DELETE FROM $TABLE_CHILDREN")
    fun deleteTable()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(child: Child)

    @Query("SELECT * FROM $TABLE_CHILDREN")
    fun queryAll(): List<Child>

    @Transaction
    fun updateMultiple(children: List<Child>) {
        val savedChildren = queryAll()
        insertMultiple(children)

        savedChildren.forEach {
            if(!children.contains(it)) {
                delete(it)
            }
        }
    }
}