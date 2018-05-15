package com.marijannovak.autismhelper.common.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.common.fragments.LoadingDialog
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.createFactory
import com.marijannovak.autismhelper.utils.logTag
import org.jetbrains.anko.toast
import javax.inject.Inject


abstract class ViewModelActivity<V : BaseViewModel<M>, M> : AppCompatActivity() {

    @Inject
    lateinit var viewModel: V

    protected var pbLoading: LoadingDialog? = null

    abstract fun handleResource(resource: Resource<List<M>>?)
    abstract fun subscribeToData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = viewModel.createFactory()
        ViewModelProviders.of(this, viewModelFactory).get(viewModel.javaClass)
        Log.e(logTag(), "ViewModelInstance $viewModel")

        subscribeToData()
    }

    fun showError(msgResId: Int, message: String?) {
        if (msgResId <= 0) {
            toast(message!!)
        } else {
            toast(msgResId)
        }
    }

    fun showLoading(status: Status) {
        pbLoading?.dismiss()
        if (status == Status.LOADING) {
            pbLoading = pbLoading ?: LoadingDialog()
            if (!pbLoading!!.isAdded) {
                pbLoading!!.show(supportFragmentManager, "")
            }
        } else {
            pbLoading = null
        }
    }

}