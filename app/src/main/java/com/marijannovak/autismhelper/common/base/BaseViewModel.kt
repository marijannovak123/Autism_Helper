package com.marijannovak.autismhelper.common.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.marijannovak.autismhelper.common.enums.Enums.State
import io.reactivex.disposables.CompositeDisposable

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
abstract class BaseViewModel<T> : ViewModel() {

    protected var contentLiveData = MutableLiveData<T>()
    protected var errorLiveData = MutableLiveData<Throwable>()
    protected var stateLiveData = MutableLiveData<State>()

    protected var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getContentLD() : MutableLiveData<T> = this.contentLiveData
    fun getErrorLD() : MutableLiveData<Throwable> = this.errorLiveData
    fun getStateLD() : MutableLiveData<State> = this.stateLiveData

}