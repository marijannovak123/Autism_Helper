package com.marijannovak.autismhelper.data.repo

import io.reactivex.Single

/**
 * Created by Marijan on 26.3.2018..
 */
interface IDataRepository {
    fun syncData() : Single<Boolean>
    fun deleteDataTables()
    fun logOut()
    fun getParentPassword(): String
    fun saveParentPassword(password: String)
}