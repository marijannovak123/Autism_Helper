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
        compositeDisposable.add(
                repository.loadCategories().subscribe(
                        {categories -> resourceLiveData.value = Resource.success(categories) },
                        {error -> resourceLiveData.value = Resource.message(R.string.data_load_error) }
                )
        )
    }

}