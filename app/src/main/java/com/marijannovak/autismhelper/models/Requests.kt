package com.marijannovak.autismhelper.models

import java.io.Serializable

data class SignupRequest(
        var email: String,
        var username: String,
        var password: String
) : Serializable