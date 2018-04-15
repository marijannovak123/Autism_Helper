package com.marijannovak.autismhelper.common.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.data.repo.IDataRepository
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Base class for ViewModels
 * @param T represents data model used
 *
 * @param contentLiveData store model data and observe it from lifecycle owner (activity, fragment)
 * @param errorLiveData show message on change
 * @param stateLiveData indicates current state - loading, message or showing content
 *
 * @param compositeDisposable collect Rx disposables and dispose to prevent memory leaks
 */
abstract class BaseViewModel<T>(private val dataRepository: IDataRepository) : ViewModel() {

    val resourceLiveData = MutableLiveData<Resource<List<T>>>()

    protected var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
        AppDatabase.closeDB()
        super.onCleared()
    }

    fun logOut() {
        dataRepository.deleteDataTables()
        dataRepository.logOut()

        resourceLiveData.value = Resource.home()
    }

    fun getParentPassword() = dataRepository.getParentPassword()

    fun saveParentPassword(password: String) {
        dataRepository.saveParentPassword(password)
    }

}