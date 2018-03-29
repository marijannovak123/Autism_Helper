package com.marijannovak.autismhelper.sync

import io.reactivex.Single

/**
 * Created by Marijan on 26.3.2018..
 */
interface ISyncRepository {
    fun syncData() : Single<Boolean>
    fun deleteDataTables()
}