package com.marijannovak.autismhelper.modules.child

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.modules.child.mvvm.ChildViewModel
import com.marijannovak.autismhelper.utils.Resource

class ChildActivity : ViewModelActivity<ChildViewModel, Child>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child)
    }

    override fun createViewModel() =  ViewModelProviders.of(this).get(ChildViewModel::class.java)



    override fun subscribeToData() {

    }

    override fun handleResource(resource: Resource<List<Child>>?) {

    }
}
