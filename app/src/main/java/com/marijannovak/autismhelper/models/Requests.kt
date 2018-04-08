package com.marijannovak.autismhelper.models

data class SignupRequest(
        var email: String,
        var username: String,
        var password: String,
        var children : List<Child>
)