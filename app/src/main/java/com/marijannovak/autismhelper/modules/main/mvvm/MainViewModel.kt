package com.marijannovak.autismhelper.modules.main.mvvm

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by Marijan on 23.3.2018..
 */
class MainViewModel @Inject constructor(private val repository : MainRepository)
    : BaseViewModel<Child>() {

    fun getChildrenToPick() {
        repository.getChildren().subscribe(object: SingleObserver<List<Child>> {
            override fun onSuccess(children: List<Child>?) {
                resourceLiveData.value = Resource.success(children)
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                resourceLiveData.value = Resource.message(R.string.children_load_fail)
            }
        })
    }

}