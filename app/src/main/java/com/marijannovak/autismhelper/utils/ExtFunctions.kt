package com.marijannovak.autismhelper.utils

import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.models.User

fun FirebaseUser.mapToUser() = User(this.displayName ?: this.email, this.uid, this.email)