package com.marijannovak.autismhelper.modules.child.mvvm

import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.repo.IDataRepository

class ChildViewModel(private val repository: IChildRepository, dataRepository: IDataRepository) : BaseViewModel<Any>(dataRepository) {
}