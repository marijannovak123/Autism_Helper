package com.marijannovak.autismhelper.modules.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.config.Constants.Companion.KEY_SIGNUP_REQUEST
import com.marijannovak.autismhelper.config.Constants.Companion.RESULT_CODE_GOOGLE_SIGNIN
import com.marijannovak.autismhelper.config.Constants.Companion.RESULT_CODE_SIGNUP
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.modules.login.mvvm.LoginViewModel
import com.marijannovak.autismhelper.modules.main.MainActivity
import com.marijannovak.autismhelper.utils.DialogHelper
import com.marijannovak.autismhelper.utils.InputValidator
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.design.snackbar

class LoginActivity : ViewModelActivity<LoginViewModel, User>() {

    private var childrenList: List<Child> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initListeners()
        customizeGoogleSignInButton()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkLoggedIn()
    }

    private fun initListeners() {
        btnLogin.setOnClickListener { attemptLogin() }
        btnGoogleSignIn.setOnClickListener { googleSignIn() }
        btnRegister.setOnClickListener { startSignUpActivity() }
        tvForgotPassword.setOnClickListener { forgotPasswordDialog() }
    }

    override fun subscribeToData() {
        viewModel.resourceLiveData.observe(this, Observer {
            resource ->
                resource?.let {
                    handleResource(it)
                }
        })
    }

    private fun customizeGoogleSignInButton() {
        val tvGoogleSignIn = btnGoogleSignIn.getChildAt(0) as TextView
        tvGoogleSignIn.text = getString(R.string.google_sign_in)
    }

    private fun startMainActivity() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0,0)
        finish()
    }

    private fun addChildDialog(user: User?) {
        user?.let {
            DialogHelper.showAddChildDialog(this, user.id, childrenList.size, {
                child, another ->
                            childrenList+= child
                            if(another) {
                                addChildDialog(user)
                            } else {
                                val userWithChildren = user.copy(children = childrenList)
                                viewModel.insertToFirebase(userWithChildren)
                            }
            })
        }

    }

    private fun snackbarMessage(message: String?) {
        snackbar(llContent, message ?: getString(R.string.error))
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
                viewModel.register(signupRequest)
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

        if(valid)
            viewModel.login(email, password)
        else
            snackbarMessage(getString(R.string.input_errors))
    }

    private fun forgotPasswordDialog() {
        DialogHelper.showForgotPasswordDialog(this, {email -> viewModel.forgotPassword(email)})
    }

    override fun handleResource(resource: Resource<List<User>>?) {
        resource?.let {
            when (it.status) {
                Status.SUCCESS -> {
                    startMainActivity()
                }

                Status.MESSAGE -> {
                    showLoading(false)
                    showError(0, it.message)
                }

                Status.LOADING -> {
                    showLoading(true)
                }

                Status.SIGNEDUP -> {
                    addChildDialog(it.data!![0])
                }

                else -> {
                    showLoading(false)
                }
            }
        }
    }
}
