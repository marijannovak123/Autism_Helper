package com.marijannovak.autismhelper.data.models

import java.io.Serializable

data class SignupRequest(
        var email: String,
        var username: String,
        var password: String
) : Serializable

data class ParentPasswordRequest(
        var parentPassword: String
)

data class UserUpdateRequest(
        var username: String,
        var parentPassword: String
)
