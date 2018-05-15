package com.marijannovak.autismhelper.common.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
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
        viewModel = ViewModelProviders.of(activity as FragmentActivity, factory).get(viewModel.javaClass)
        Log.e(logTag(), "ViewModelInstance $viewModel")
    }

}