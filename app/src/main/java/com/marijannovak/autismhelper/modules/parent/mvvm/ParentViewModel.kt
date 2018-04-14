package com.marijannovak.autismhelper.modules.parent.mvvm

import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.data.repo.IDataRepository

class ParentViewModel(private val repository: IParentRepository,
                      dataRepository: IDataRepository)
    : BaseViewModel<Any>(dataRepository) {

    constructor() : this(ParentRepository(), DataRepository())
}