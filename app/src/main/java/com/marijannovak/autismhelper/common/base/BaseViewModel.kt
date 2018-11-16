package com.marijannovak.autismhelper.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.repositories.DataRepository
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.SingleEventLiveData
import com.marijannovak.autismhelper.utils.State
import com.marijannovak.autismhelper.utils.Status
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

open class BaseViewModel<M>
@Inject constructor(): ViewModel() {

    @Inject lateinit var dataRepository: DataRepository

    private var dataLiveData = MutableLiveData<M>()
    private var statusLiveData = SingleEventLiveData<Status>()

    val data: LiveData<M>
        get() = dataLiveData

    val status: LiveData<Status>
        get() = statusLiveData

    private val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val resourceLiveData = MutableLiveData<Resource<M>>()

    protected var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        viewModelJob.cancel()
        compositeDisposable.clear()
        compositeDisposable.dispose()
        super.onCleared()
    }

    protected fun setData(data: M?) {
        dataLiveData.value = data
        statusLiveData.value = Status(state = State.SUCCESS)
    }

    protected fun setError(error: Throwable) {
        statusLiveData.value = Status(state = State.MESSAGE, message = error.message ?: "", messageId = R.string.error)
    }

    protected fun setMessage(message: String) {
        statusLiveData.value = Status(state = State.MESSAGE, message = message, messageId = null)
    }

    protected fun setLoading() {
        statusLiveData.value = Status(state = State.LOADING)
    }

    protected fun setSuccess() {
        statusLiveData.value = Status(state = State.SUCCESS)
    }

    fun logOut() {
        dataRepository.logOut().subscribe(object : CompletableObserver {
            override fun onComplete() {
                resourceLiveData.value = Resource.home()
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable) {
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