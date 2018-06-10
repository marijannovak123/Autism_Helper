package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_SENTENCE
import com.marijannovak.autismhelper.data.models.SavedSentence
import io.reactivex.Flowable

@Dao
interface SavedSentenceDao: BaseDao<SavedSentence> {

    @Query("SELECT * FROM $TABLE_SENTENCE")
    fun getSavedSentences(): Flowable<List<SavedSentence>>

    @Query("DELETE FROM $TABLE_SENTENCE")
    fun deleteTable()

}