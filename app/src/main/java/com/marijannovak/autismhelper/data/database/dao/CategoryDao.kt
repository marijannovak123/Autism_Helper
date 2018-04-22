package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.*
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_CATEGORIES
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Marijan on 26.3.2018..
 */
@Dao
interface CategoryDao: BaseDao<Category> {

    @Transaction
    @Query("SELECT * FROM $TABLE_CATEGORIES")
    fun getCategories() : Single<List<CategoryQuestionsAnswersJoin>>

    @Transaction
    @Query("SELECT * FROM $TABLE_CATEGORIES WHERE id = :id")
    fun getCategoryById(id : Int) : Single<CategoryQuestionsAnswersJoin>

    @Query("SELECT COUNT(*) FROM $TABLE_CATEGORIES")
    fun getCategoryCount() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCategory(category: Category)

    @Query("DELETE FROM $TABLE_CATEGORIES")
    fun deleteTable()
}