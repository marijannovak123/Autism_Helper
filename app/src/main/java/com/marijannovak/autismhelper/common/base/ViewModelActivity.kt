package com.marijannovak.autismhelper.common.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.marijannovak.autismhelper.common.fragments.LoadingDialog
import com.marijannovak.autismhelper.utils.Resource
import org.jetbrains.anko.toast

abstract class ViewModelActivity<V : BaseViewModel<M>, M> : AppCompatActivity() {

    protected lateinit var viewModel : V
    protected var pbLoading : LoadingDialog? = null

    protected abstract fun createViewModel() : V
    protected abstract fun handleResource(resource: Resource<List<M>>?)
    protected abstract fun subscribeToData()


    protected fun showError(msgResId: Int, message: String?) {
        if(msgResId <= 0) {
            toast(message!!)
        } else {
            toast(msgResId)
        }
    }

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