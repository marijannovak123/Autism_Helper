package com.marijannovak.autismhelper.common.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.marijannovak.autismhelper.common.enums.Enums.State
import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.data.repo.IDataRepository
import com.marijannovak.autismhelper.utils.ErrorHelper.Companion.unknownError
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Base class for ViewModels
 * @param T represents data model used
 *
 * @param contentLiveData store model data and observe it from lifecycle owner (activity, fragment)
 * @param errorLiveData show error on change
 * @param stateLiveData indicates current state - loading, error or showing content
 *
 * @param compositeDisposable collect Rx disposables and dispose to prevent memory leaks
 */
abstract class BaseViewModel<T>(private val dataRepository: IDataRepository) : ViewModel() {

    protected var contentLiveData = MutableLiveData<List<T>>()
    protected var errorLiveData = MutableLiveData<Throwable>()
    protected var stateLiveData = MutableLiveData<State>()

    protected var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        AppDatabase.closeDB()
        super.onCleared()
    }

    fun getContentLD() : MutableLiveData<List<T>> = this.contentLiveData
    fun getErrorLD() : MutableLiveData<Throwable> = this.errorLiveData
    fun getStateLD() : MutableLiveData<State> = this.stateLiveData

    fun syncData() {
        dataRepository.syncData().subscribe(object : SingleObserver<Boolean> {
            override fun onSuccess(syncDone: Boolean?) {
                if(syncDone!!) {
                    stateLiveData.value = State.NEXT
                } else {
                    stateLiveData.value = State.ERROR
                    errorLiveData.value = unknownError()
                }
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                dataRepository.deleteDataTables()
                stateLiveData.value = State.ERROR
                errorLiveData.value = e ?: unknownError()
            }
        })
    }

    fun logOut() {
        dataRepository.deleteDataTables()
        dataRepository.logOut()

        stateLiveData.value = State.HOME
    }

    fun getParentPassword() = dataRepository.getParentPassword()

    fun saveParentPassword(password: String) {
        dataRepository.saveParentPassword(password)
    }

}