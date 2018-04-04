package com.marijannovak.autismhelper.modules.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Enums.State
import com.marijannovak.autismhelper.common.listeners.LoginSignupListener
import com.marijannovak.autismhelper.models.SignupRequest
import com.marijannovak.autismhelper.models.User
import com.marijannovak.autismhelper.modules.login.adapters.LoginSignupPagerAdapter
import com.marijannovak.autismhelper.modules.login.mvvm.LoginRepository
import com.marijannovak.autismhelper.modules.login.mvvm.LoginViewModel
import com.marijannovak.autismhelper.modules.main.MainActivity
import com.marijannovak.autismhelper.sync.SyncRepository
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : ViewModelActivity<LoginViewModel, User>(), LoginSignupListener {

    private var pagerAdapter: LoginSignupPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel.checkLogin()

        initPagerAdapter()
    }

    private fun initPagerAdapter() {
        pagerAdapter = LoginSignupPagerAdapter(supportFragmentManager)
        pagerLoginSignup.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pagerLoginSignup)
    }

    //private fun init() {//todo: get from edittexts and validate
    //    btnRegister.setOnClickListener { viewModel.register("marijannovak123@gmail.com", "lozinka123") }
    //    btnLogin.setOnClickListener { viewModel.login("marijannovak123@gmail.com", "lozinka123") }
    //}

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
        finish()
    }

    override fun showError(throwable: Throwable) {
        toast(throwable.message.toString())
    }

    override fun handleState(state : State) {
       //when(state) {
       //    State.LOADING -> {
       //        pbLoading.show()
       //        llContent.visibility = View.GONE
       //    }

       //    State.NEXT -> startMainActivity()

       //    else -> {
       //        pbLoading.hide()
       //        llContent.visibility = View.VISIBLE
       //    }
       //}
    }

    override fun onLogin(email: String, password: String) {
        Snackbar.make(pbLoading, "login", 0)
    }

    override fun onSignup(signupRequest: SignupRequest) {
        Snackbar.make(pbLoading, "signup", 0)
    }
}
