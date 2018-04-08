package com.marijannovak.autismhelper.utils

import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.models.SignupRequest
import com.marijannovak.autismhelper.models.User

fun FirebaseUser.mapToUser(singupRequest: SignupRequest)
        = User(singupRequest.username, this.uid, singupRequest.email, singupRequest.children)

fun FirebaseUser.mapToUser() = User(this.displayName, this.uid, this.email, ArrayList())