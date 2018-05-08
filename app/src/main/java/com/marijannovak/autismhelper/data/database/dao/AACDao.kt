package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_AAC
import com.marijannovak.autismhelper.data.models.AacPhrase
import io.reactivex.Flowable

@Dao
interface AACDao: BaseDao<AacPhrase> {

    @Query("SELECT * FROM $TABLE_AAC")
    fun getAllPhrases(): Flowable<List<AacPhrase>>

}