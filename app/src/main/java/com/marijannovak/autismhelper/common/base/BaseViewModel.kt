package com.marijannovak.autismhelper.common.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

open class BaseViewModel<T>
@Inject constructor(
        private val dataRepository: DataRepository)
    : ViewModel() {

    val resourceLiveData = MutableLiveData<Resource<List<T>>>()

    protected var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun logOut() {
        dataRepository.logOut().subscribe(object : CompletableObserver {
            override fun onComplete() {
                resourceLiveData.value = Resource.home()
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                resourceLiveData.value = Resource.message(R.string.logout_not_success, e!!.message ?: "")
            }
        })
    }

    fun isSoundOn() = dataRepository.isSoundOn()

    fun getParentPassword() = dataRepository.getParentPassword()

    fun saveParentPassword(password: String) {
        resourceLiveData.value = Resource.loading()
        dataRepository.saveParentPassword(password).subscribe(
                { resourceLiveData.value = Resource.next() },
                { resourceLiveData.value = Resource.message(R.string.error_saving, it.message ?: "" ) }
        )
    }

    fun getTtsSpeed() = dataRepository.getTtsSpeed()

    fun getTtsPitch() = dataRepository.getTtsPitch()
}