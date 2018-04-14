package com.marijannovak.autismhelper.modules.child

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Enums
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.modules.child.mvvm.ChildRepository
import com.marijannovak.autismhelper.modules.child.mvvm.ChildViewModel

class ChildActivity : ViewModelActivity<ChildViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child)
    }

    override fun createViewModel() =  ViewModelProviders.of(this).get(ChildViewModel::class.java)

    override fun handleState(state: Enums.State) {

    }

    override fun showError(throwable: Throwable) {

    }

    override fun subscribeToData() {

    }
}
