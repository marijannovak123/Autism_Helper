package com.marijannovak.autismhelper.common.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.common.fragments.LoadingDialog
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.createFactory
import org.jetbrains.anko.toast
import javax.inject.Inject


abstract class ViewModelActivity<V : BaseViewModel<M>, M> : AppCompatActivity() {

    @Inject
    lateinit var viewModel : V

    protected var pbLoading : LoadingDialog? = null

    abstract fun handleResource(resource: Resource<List<M>>?)
    abstract fun subscribeToData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = viewModel.createFactory()
        ViewModelProviders.of(this, viewModelFactory).get(viewModel.javaClass)

        subscribeToData()
    }

    protected fun showError(msgResId: Int, message: String?) {
        if(msgResId <= 0) {
            toast(message!!)
        } else {
            toast(msgResId)
        }
    }

    protected fun showLoading(status: Status) {
        if(status == Status.LOADING) {
            pbLoading = pbLoading ?: LoadingDialog()
            pbLoading!!.show(supportFragmentManager, "")
        }
        else {
            pbLoading?.dismiss()
        }
    }

}