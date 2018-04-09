package com.marijannovak.autismhelper.modules.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.config.Constants.Companion.KEY_SIGNUP_REQUEST
import com.marijannovak.autismhelper.models.Child
import com.marijannovak.autismhelper.models.SignupRequest
import com.marijannovak.autismhelper.utils.InputValidator
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.design.snackbar
import java.util.*

class SignUpActivity : AppCompatActivity() {
//todo: add children dialog
    private var childrenList : List<Child> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnSignUp.setOnClickListener { attemptSignup() }
    }

    private fun attemptSignup() {
        val email = etEmail.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        //this.childrenList += Child(1,"Koko", "M", "mmm", 213123123)

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

        if (!InputValidator.validate(username, Constants.VALIDATION_USERNAME)) {
            valid = false
            etUsername.error = resources.getString(R.string.username_invalid)
        } else {
            etUsername.error = null
        }

        if (!password.equals(confirmPassword)) {
            valid = false
            etConfirmPassword.error = resources.getString(R.string.passwords_not_same)
        } else {
            etConfirmPassword.error = null
        }

        if(valid)
            sendRequest(SignupRequest(email, username, password, this.childrenList))
        else
            snackbar(btnSignUp, R.string.input_errors)
    }

    private fun sendRequest(signupRequest: SignupRequest) {
        val returnIntent = Intent()
        returnIntent.putExtra(KEY_SIGNUP_REQUEST, signupRequest)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
