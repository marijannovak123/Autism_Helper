package com.marijannovak.autismhelper.common.base

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import android.util.Log
import com.marijannovak.autismhelper.utils.createFactory
import com.marijannovak.autismhelper.utils.logTag
import javax.inject.Inject

open class InjectableFragment<V : BaseViewModel<*>> : BaseFragment() {

    @Inject
    lateinit var viewModel: V

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = viewModel.createFactory()
        viewModel = ViewModelProviders.of(this, factory).get(viewModel.javaClass)
    }

}