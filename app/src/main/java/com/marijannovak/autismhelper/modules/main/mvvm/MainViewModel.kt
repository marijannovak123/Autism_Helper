package com.marijannovak.autismhelper.modules.main.mvvm

import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.enums.Enums
import com.marijannovak.autismhelper.models.Category
import io.reactivex.subscribers.DisposableSubscriber

/**
 * Created by Marijan on 23.3.2018..
 */
class MainViewModel(private val repository : IMainRepository)
    : BaseViewModel<Category>() {

    fun loadCategories() {
        stateLiveData.value = Enums.State.LOADING
        compositeDisposable.add(
                repository.loadCategories().subscribeWith(object : DisposableSubscriber<List<Category>>() {
                    override fun onComplete() {
                        stateLiveData.value = Enums.State.CONTENT
                    }

                    override fun onNext(categories: List<Category>?) {
                        if(categories != null) {
                            contentLiveData.value = categories
                        } else {
                            errorLiveData.value = Throwable("Null response")
                        }

                    }

                    override fun onError(t: Throwable?) {
                        stateLiveData.value = Enums.State.ERROR
                        t?.let {
                            errorLiveData.value = it
                        }
                    }
                })
        )
    }
}