package com.marijannovak.autismhelper.viewmodels

import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.repositories.MainRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Marijan on 23.3.2018..
 */
class MainViewModel @Inject constructor(
        private val repository: MainRepository
): BaseViewModel<List<Child>>() {

    fun getChildrenToPick() {
        uiScope.launch {
            val childrenChannel = repository.getChildren()
            for(children in childrenChannel) {
                setData(children)
            }
        }
    }

}