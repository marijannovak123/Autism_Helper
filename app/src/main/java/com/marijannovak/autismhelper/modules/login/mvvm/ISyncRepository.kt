package com.marijannovak.autismhelper.modules.login.mvvm

import io.reactivex.SingleObserver

/**
 * Created by Marijan on 26.3.2018..
 */
interface ISyncRepository {
    fun syncData(observer : SingleObserver<Boolean>)
    fun deleteDataTables()
}