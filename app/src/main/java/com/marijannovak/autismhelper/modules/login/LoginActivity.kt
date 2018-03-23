package com.marijannovak.autismhelper.modules.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Enums.State
import com.marijannovak.autismhelper.modules.login.mvvm.LoginRepository
import com.marijannovak.autismhelper.modules.login.mvvm.LoginViewModel
import com.marijannovak.autismhelper.modules.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : ViewModelActivity<LoginViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel.checkLogin()
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel(LoginRepository())
    }

    override fun subscribeToData() {
        viewModel.getContentLD().observe(this, Observer { startMainActivity() } )
        viewModel.getErrorLD().observe(this, Observer { throwable -> showError(throwable!!) })
        viewModel.getStateLD().observe(this, Observer { state -> handleState(state!!) })
    }

    private fun startMainActivity() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showError(throwable: Throwable) {
        toast(throwable.message.toString())
    }

    private fun handleState(state : State) {
        when(state) {
            State.LOADING -> pbLoading.show()
            else -> pbLoading.hide()
        }
    }
}
