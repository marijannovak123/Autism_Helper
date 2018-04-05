package com.marijannovak.autismhelper.common.listeners

import com.marijannovak.autismhelper.models.SignupRequest

interface LoginSignupListener {
    fun onLogin(email: String, password: String)
    fun onGoogleSignIn()
    fun onSignup(signupRequest: SignupRequest)
}