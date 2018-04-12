package com.marijannovak.autismhelper.data.models

import java.io.Serializable

data class SignupRequest(
        var email: String,
        var username: String,
        var password: String
) : Serializable