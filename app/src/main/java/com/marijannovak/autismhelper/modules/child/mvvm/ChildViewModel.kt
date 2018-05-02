package com.marijannovak.autismhelper.modules.child.mvvm

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ChildViewModel @Inject constructor(private val repository: ChildRepository)
    : BaseViewModel<Category>() {

    fun loadCategories() {
        resourceLiveData.value = Resource.loading()
        repository.loadCategories().subscribe(object: SingleObserver<List<Category>> {
            override fun onSuccess(categories: List<Category>?) {
                resourceLiveData.value = Resource.success(categories)
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                resourceLiveData.value = Resource.message(R.string.data_load_error)
            }

        })

    }

}