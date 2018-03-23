package com.marijannovak.autismhelper.common.base

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class ViewModelActivity<T : ViewModel> : AppCompatActivity() {

    protected lateinit var viewModel : T

    protected abstract fun createViewModel() : T
    protected abstract fun subscribeToData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewModel = createViewModel()
        subscribeToData()
    }

}