package com.marijannovak.autismhelper.modules.main.mvvm

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.utils.Resource
import javax.inject.Inject

/**
 * Created by Marijan on 23.3.2018..
 */
class MainViewModel @Inject constructor(
        private val repository: MainRepository,
        dataRepository: DataRepository)
    : BaseViewModel<List<Child>>(dataRepository) {

    fun getChildrenToPick() {
        compositeDisposable.add(
                repository.getChildren().subscribe(
                        { children -> resourceLiveData.value = Resource.success(children) },
                        { resourceLiveData.value = Resource.message(R.string.children_load_fail, it.message ?: "") }
                )
        )

    }

}