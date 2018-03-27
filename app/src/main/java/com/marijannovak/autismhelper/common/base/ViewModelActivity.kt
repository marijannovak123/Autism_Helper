package com.marijannovak.autismhelper.common.base

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.marijannovak.autismhelper.common.enums.Enums.State

abstract class ViewModelActivity<T : ViewModel, in M> : AppCompatActivity() {

    protected lateinit var viewModel : T

    protected abstract fun createViewModel() : T
    protected abstract fun handleState(state : State)
    protected abstract fun showError(throwable: Throwable)
    protected abstract fun subscribeToData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewModel = createViewModel()
        subscribeToData()
    }

}