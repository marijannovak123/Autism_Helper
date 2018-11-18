package com.marijannovak.autismhelper.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.repositories.DataRepository
import com.marijannovak.autismhelper.repositories.ResourceRepository
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

open class BaseViewModel<M> @Inject constructor(): ViewModel() {

    @Inject lateinit var resRepo: ResourceRepository
    @Inject lateinit var dataRepository: DataRepository

    private val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val resourceLiveData = MutableLiveData<Resource<M>>()

    val resource: LiveData<Resource<M>>
        get() = resourceLiveData

    protected var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        viewModelJob.cancel()
        compositeDisposable.clear()
        compositeDisposable.dispose()
        super.onCleared()
    }

    protected fun setData(data: M?) {
        resourceLiveData.value = Resource(Status.SUCCESS, data, null)
    }

    protected fun setMessage(msgId: Int, msg: String? = null) {
        val message = when {
            msg == null -> resRepo.getString(msgId)
            msgId == 0 -> msg
            else -> resRepo.getString(msgId) + msg
        }
        resourceLiveData.value = Resource(Status.MESSAGE, null, message)
    }


    protected fun setLoading(msgRes: Int = -1) {
        resourceLiveData.value = if(msgRes == -1) Resource.loading() else Resource.loading(msgRes)
    }

    protected fun setSuccess() {
        resourceLiveData.value = Resource.success(null)
    }

    protected fun setState(status: Status) {
        resourceLiveData.value = Resource(status, null, null)
    }

    protected fun setStateAndData(status: Status, data: M?) {
        resourceLiveData.value = Resource(status, data, null)
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
                resourceLiveData.value = Resource.message(R.string.logout_not_success, e.message ?: "")
            }
        })
    }

    fun isSoundOn() = dataRepository.isSoundOn()

    fun getParentPassword() = dataRepository.getParentPassword()

    fun saveParentPassword(password: String) {
        setLoading()
        dataRepository.saveParentPassword(password).subscribe(
                { resourceLiveData.value = Resource.next() },
                { resourceLiveData.value = Resource.message(R.string.error_saving, it.message ?: "" ) }
        )
    }

    fun getTtsSpeed() = dataRepository.getTtsSpeed()

    fun getTtsPitch() = dataRepository.getTtsPitch()
}