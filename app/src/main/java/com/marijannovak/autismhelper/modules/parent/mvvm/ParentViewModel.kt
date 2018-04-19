package com.marijannovak.autismhelper.modules.parent.mvvm

import com.marijannovak.autismhelper.common.base.BaseViewModel
import javax.inject.Inject

class ParentViewModel @Inject constructor(private val repository: ParentRepository)
    : BaseViewModel<Any>() {

}