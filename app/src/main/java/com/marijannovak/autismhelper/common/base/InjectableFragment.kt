package com.marijannovak.autismhelper.common.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.marijannovak.autismhelper.utils.createFactory
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