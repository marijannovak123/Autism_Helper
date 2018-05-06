package com.marijannovak.autismhelper.common.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

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
open class BaseViewModel<T> : ViewModel() {

    @Inject
    lateinit var dataRepository: DataRepository

    val resourceLiveData = MutableLiveData<Resource<List<T>>>()

    protected var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun logOut() {
        dataRepository.logOut().subscribe(object: CompletableObserver {
            override fun onComplete() {
                resourceLiveData.value = Resource.home()
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                resourceLiveData.value = Resource.message(R.string.logout_not_success)
            }
        })
    }

    //todo: to firebase!
    fun getParentPassword() = dataRepository.getParentPassword()

    fun saveParentPassword(password: String) {
        dataRepository.saveParentPassword(password)
    }

}