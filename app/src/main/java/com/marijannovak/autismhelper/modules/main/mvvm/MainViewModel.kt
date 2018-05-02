package com.marijannovak.autismhelper.modules.main.mvvm

import com.marijannovak.autismhelper.common.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by Marijan on 23.3.2018..
 */
class MainViewModel @Inject constructor(private val repository : MainRepository)
    : BaseViewModel<Any>() {

    //fun loadCategories() {
    //    resourceLiveData.value = Resource.loading()
    //    compositeDisposable.add(
    //            repository.loadCategories().subscribeWith(object : DisposableSubscriber<List<Category>>() {
    //                override fun onComplete() {
    //                    resourceLiveData.value = Resource.success(null)
    //                }
//
    //                override fun onNext(categories: List<Category>?) {
    //                    if(categories != null) {
    //                        resourceLiveData.value = Resource.success(categories)
    //                    } else {
    //                        resourceLiveData.value = Resource.message(R.string.load_error)
    //                    }
    //                }
//
    //                override fun onError(t: Throwable?) {
    //                    t?.let {
    //                        resourceLiveData.value = Resource.message(it.message as String)
    //                    }
    //                }
    //            })
    //    )
    //}

}