package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.config.Constants.Companion.TABLE_RSS
import com.marijannovak.autismhelper.data.models.FeedItem
import io.reactivex.Single

@Dao
interface FeedItemDao: BaseDao<FeedItem> {

    @Query("SELECT * FROM $TABLE_RSS")
    fun getItems(): Single<List<FeedItem>>
}