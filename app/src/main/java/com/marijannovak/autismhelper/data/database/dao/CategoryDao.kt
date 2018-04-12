package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CATEGORIES
import com.marijannovak.autismhelper.data.models.Category
import io.reactivex.Flowable

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface CategoryDao {

    @Query("SELECT * FROM $TABLE_CATEGORIES")
    fun getCategories() : Flowable<List<Category>>

    @Query("SELECT * FROM $TABLE_CATEGORIES WHERE id = :id")
    fun getCategoryById(id : Int) : Flowable<Category>

    @Query("SELECT COUNT(*) FROM $TABLE_CATEGORIES")
    fun getCategoryCount() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCategories(categories : List<Category>)

    @Delete
    fun delete(category: Category)

    @Delete
    fun deleteMultiple(categories : List<Category>)

    @Query("DELETE FROM $TABLE_CATEGORIES")
    fun deleteTable()
}