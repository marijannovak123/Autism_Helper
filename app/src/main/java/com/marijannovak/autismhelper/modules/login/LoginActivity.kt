package com.marijannovak.autismhelper.modules.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Enums.State
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.config.Constants.Companion.KEY_SIGNUP_REQUEST
import com.marijannovak.autismhelper.config.Constants.Companion.RESULT_CODE_GOOGLE_SIGNIN
import com.marijannovak.autismhelper.config.Constants.Companion.RESULT_CODE_SIGNUP
import com.marijannovak.autismhelper.models.SignupRequest
import com.marijannovak.autismhelper.modules.login.mvvm.LoginRepository
import com.marijannovak.autismhelper.modules.login.mvvm.LoginViewModel
import com.marijannovak.autismhelper.modules.main.MainActivity
import com.marijannovak.autismhelper.sync.SyncRepository
import com.marijannovak.autismhelper.utils.InputValidator
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.design.snackbar

class LoginActivity : ViewModelActivity<LoginViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel.checkLoggedIn()

        initListeners()
    }

    private fun initListeners() {
        btnLogin.setOnClickListener { attemptLogin() }
        btnGoogleSignIn.setOnClickListener { googleSignIn() }
        btnRegister.setOnClickListener { startSignUpActivity() }
        tvForgotPassword.setOnClickListener { attemptForgotPassword() }
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
        finish()
    }

    override fun showError(throwable: Throwable) {
        snackbarMessage(throwable.message.toString())
    }

    override fun handleState(state : State) {
       when(state) {
           State.LOADING -> {
               pbLoading.show()
               llContent.visibility = View.GONE
           }

           State.NEXT -> startMainActivity()

           State.SUCCESS -> snackbarMessage(getString(R.string.success))

           else -> {
               pbLoading.hide()
               llContent.visibility = View.VISIBLE
           }
       }
    }

    private fun snackbarMessage(message: String?) {
        snackbar(llContent, message ?: "null")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_CODE_GOOGLE_SIGNIN) {
            data?.let {
                viewModel.googleSignIn(it)
            }
        } else if (requestCode == RESULT_CODE_SIGNUP) {
            data?.let {
                val signupRequest = it.extras.getSerializable(KEY_SIGNUP_REQUEST) as SignupRequest
                signUp(signupRequest)
            }
        }
    }

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, RESULT_CODE_GOOGLE_SIGNIN)
    }

    private fun signUp(signupRequest: SignupRequest) {
       viewModel.register(signupRequest)
    }

    private fun startSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivityForResult(intent, RESULT_CODE_SIGNUP)
    }

    private fun attemptLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        var valid = true

        if (!InputValidator.validate(email, Constants.VALIDATION_EMAIL)) {
            valid = false
            etEmail.error = resources.getString(R.string.malformed_email)
        } else {
            etEmail.error = null
        }

        if (!InputValidator.validate(password, Constants.VALIDATION_PASSWORD)) {
            valid = false
            etPassword.error = resources.getString(R.string.password_invalid)
        } else {
            etPassword.error = null
        }

        if(valid)
            viewModel.login(email, password)
        else
            snackbarMessage(getString(R.string.input_errors))
    }

    private fun attemptForgotPassword() {
        val email = etEmail.text.toString().trim()
        var valid = true

        if(!InputValidator.validate(email, Constants.VALIDATION_EMAIL)) {
            valid = false
            etEmail.error = resources.getString(R.string.error_invalid_email)
        } else {
            etEmail.error = null
        }

        if(valid)
            viewModel.forgotPassword(email)
        else
            snackbarMessage(getString(R.string.input_errors))

    }
}
