package com.marijannovak.autismhelper.modules.main.mvvm

import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.enums.Enums.State
import com.marijannovak.autismhelper.models.Category
import com.marijannovak.autismhelper.sync.ISyncRepository
import io.reactivex.subscribers.DisposableSubscriber

/**
 * Created by Marijan on 23.3.2018..
 */
class MainViewModel(private val repository : IMainRepository, private val syncRepository: ISyncRepository)
    : BaseViewModel<Category>() {

    fun loadCategories() {
        stateLiveData.value = State.LOADING
        compositeDisposable.add(
                repository.loadCategories().subscribeWith(object : DisposableSubscriber<List<Category>>() {
                    override fun onComplete() {
                        stateLiveData.value = State.CONTENT
                    }

                    override fun onNext(categories: List<Category>?) {
                        if(categories != null) {
                            contentLiveData.value = categories
                        } else {
                            errorLiveData.value = Throwable("Null response")
                        }

                    }

                    override fun onError(t: Throwable?) {
                        stateLiveData.value = State.ERROR
                        t?.let {
                            errorLiveData.value = it
                        }
                    }
                })
        )
    }

    fun logout() {
        syncRepository.deleteDataTables()
        repository.logout()

        stateLiveData.value = State.HOME
    }
}