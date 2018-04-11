package com.marijannovak.autismhelper.modules.parent

import android.os.Bundle
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Enums
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentRepository
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel

class ParentActivity : ViewModelActivity<ParentViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)
    }

    override fun createViewModel() = ParentViewModel(ParentRepository())


    override fun subscribeToData() {

    }

    override fun handleState(state: Enums.State) {

    }

    override fun showError(throwable: Throwable) {

    }
}
