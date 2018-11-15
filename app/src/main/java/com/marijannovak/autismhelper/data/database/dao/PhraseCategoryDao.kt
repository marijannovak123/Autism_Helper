package com.marijannovak.autismhelper.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_PHRASE_CATEGORY
import com.marijannovak.autismhelper.data.models.PhraseCategory
import io.reactivex.Flowable

@Dao
interface PhraseCategoryDao: BaseDao<PhraseCategory> {

    @Query("SELECT * FROM $TABLE_PHRASE_CATEGORY")
    fun getPhraseCategories(): Flowable<List<PhraseCategory>>
}