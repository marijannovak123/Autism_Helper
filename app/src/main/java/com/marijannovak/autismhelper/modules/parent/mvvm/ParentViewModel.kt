package com.marijannovak.autismhelper.modules.parent.mvvm

import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.repo.DataRepository
import javax.inject.Inject

class ParentViewModel @Inject constructor(private val repository: ParentRepository,
                                              dataRepository: DataRepository)
    : BaseViewModel<Any>(dataRepository) {

}