package com.marijannovak.autismhelper.viewmodels

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.repositories.DataRepository
import com.marijannovak.autismhelper.repositories.ChildRepository
import com.marijannovak.autismhelper.utils.Resource
import javax.inject.Inject

class ChildViewModel
@Inject constructor(
        private val repository: ChildRepository
) : BaseViewModel<List<Category>>() {

    fun loadCategories() {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.loadCategories().subscribe(
                        { categories -> resourceLiveData.value = Resource.success(categories) },
                        { error -> resourceLiveData.value = Resource.message(R.string.data_load_error, error.message ?: "") }
                )
        )
    }

}