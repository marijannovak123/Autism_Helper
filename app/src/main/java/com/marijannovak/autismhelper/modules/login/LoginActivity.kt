package com.marijannovak.autismhelper.modules.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Enums.State
import com.marijannovak.autismhelper.models.User
import com.marijannovak.autismhelper.modules.login.mvvm.LoginRepository
import com.marijannovak.autismhelper.modules.login.mvvm.LoginViewModel
import com.marijannovak.autismhelper.modules.login.mvvm.SyncRepository
import com.marijannovak.autismhelper.modules.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : ViewModelActivity<LoginViewModel>() {
//todo: tab layout register/login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

        viewModel.checkLogin()
    }

    private fun init() {//todo: get from edittexts and validate
        btnRegister.setOnClickListener { viewModel.register("marijannovak123@gmail.com", "lozinka123") }
        btnLogin.setOnClickListener { viewModel.login("marijannovak123@gmail.com", "lozinka123") }
    }

    override fun createViewModel(): LoginViewModel {
        return LoginViewModel(LoginRepository(), SyncRepository())
    }

    override fun subscribeToData() {
        viewModel.getContentLD().observe(this, Observer { startSync() } )
        viewModel.getErrorLD().observe(this, Observer { throwable -> showError(throwable!!) })
        viewModel.getStateLD().observe(this, Observer { state -> handleState(state!!) })
    }

    private fun startSync() {
        viewModel.syncData()
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
            State.NEXT -> startMainActivity()
            else -> pbLoading.hide()
        }
    }
}
