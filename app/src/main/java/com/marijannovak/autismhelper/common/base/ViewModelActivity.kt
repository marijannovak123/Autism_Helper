package com.marijannovak.autismhelper.common.base

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.marijannovak.autismhelper.common.enums.Enums.State
import com.marijannovak.autismhelper.common.fragments.LoadingDialog

abstract class ViewModelActivity<T : ViewModel> : AppCompatActivity() {

    protected lateinit var viewModel : T
    protected var pbLoading : LoadingDialog? = null

    protected abstract fun createViewModel() : T
    protected abstract fun handleState(state : State)
    protected abstract fun showError(throwable: Throwable)
    protected abstract fun subscribeToData()

    protected fun showLoading(visible: Boolean) {
        if(visible) {
            pbLoading = pbLoading ?: LoadingDialog()
            pbLoading!!.show(supportFragmentManager, "")
        }
        else {
            pbLoading?.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //supportActionBar?.setDisplayShowTitleEnabled(false)

        this.viewModel = createViewModel()
        subscribeToData()
    }

}