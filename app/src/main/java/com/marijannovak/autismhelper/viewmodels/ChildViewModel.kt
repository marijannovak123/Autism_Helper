package com.marijannovak.autismhelper.viewmodels

import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.repositories.ChildRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChildViewModel @Inject constructor(
        private val repository: ChildRepository
) : BaseViewModel<List<Category>>() {

    fun loadCategories() {
        setLoading()
        uiScope.launch {
            val categoryChannel = repository.loadCategories()
            for(categories in categoryChannel) {
                setData(categories)
            }
        }
    }

}