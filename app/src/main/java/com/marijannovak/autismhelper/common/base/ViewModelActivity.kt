package com.marijannovak.autismhelper.common.base

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.common.fragments.LoadingDialog
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.createFactory
import com.marijannovak.autismhelper.utils.logTag
import org.jetbrains.anko.design.snackbar
import javax.inject.Inject


abstract class ViewModelActivity<V : BaseViewModel<M>, M> : AppCompatActivity() {

    @Inject
    lateinit var viewModel: V

    protected var pbLoading: LoadingDialog? = null

    abstract fun handleResource(resource: Resource<M>?)
    abstract fun subscribeToData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = viewModel.createFactory()
        ViewModelProviders.of(this, viewModelFactory).get(viewModel.javaClass)
        Log.e(logTag(), "ViewModelInstance $viewModel")

        subscribeToData()
    }

    fun showMessage(msgResId: Int, message: String?) {
        val view = this.window.decorView.findViewById<View>(android.R.id.content)
        if (msgResId <= 0) {
            snackbar(view, message!!)
        } else {
           snackbar(view, msgResId)
        }
    }

    fun showLoading(status: Status, message: String?) {
        if (status == Status.LOADING) {
            if(pbLoading == null) {
                pbLoading = LoadingDialog()
                if (!pbLoading!!.isAdded) {
                    pbLoading!!.showWithMessage(supportFragmentManager, message)
                }
            } else {
                pbLoading!!.updateMessage(message)
            }

        } else {
            pbLoading?.dismiss()
            pbLoading = null
        }
    }

}