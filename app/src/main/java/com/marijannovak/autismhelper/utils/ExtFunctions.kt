package com.marijannovak.autismhelper.utils

import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User

fun FirebaseUser.mapToUser(singupRequest: SignupRequest)
        = User(singupRequest.username, this.uid, singupRequest.email, ArrayList())

fun FirebaseUser.mapToUser() = User(this.displayName, this.uid, this.email, ArrayList())