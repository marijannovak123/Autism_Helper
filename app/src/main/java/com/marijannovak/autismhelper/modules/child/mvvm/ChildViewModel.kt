package com.marijannovak.autismhelper.modules.child.mvvm

import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.Child
import javax.inject.Inject

class ChildViewModel @Inject constructor(private val repository: ChildRepository) : BaseViewModel<Child>() {

}