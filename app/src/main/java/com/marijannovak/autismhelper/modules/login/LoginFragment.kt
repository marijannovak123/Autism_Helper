package com.marijannovak.autismhelper.modules.login

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.listeners.LoginSignupListener
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.utils.InputValidator
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.toast

class LoginFragment : Fragment() {

    private var listener: LoginSignupListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener { attemptLogin() }
        btnGoogleSignIn.setOnClickListener { listener!!.onGoogleSignIn() }
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
            listener!!.onLogin(email, password)
        else
            toast(R.string.input_errors)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginSignupListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement LoginSignupListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
