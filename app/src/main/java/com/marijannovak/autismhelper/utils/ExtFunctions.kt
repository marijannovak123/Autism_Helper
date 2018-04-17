package com.marijannovak.autismhelper.utils

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User

fun FirebaseUser.mapToUser(singupRequest: SignupRequest)
        = User(singupRequest.username, this.uid, singupRequest.email, ArrayList())

fun FirebaseUser.mapToUser() = User(this.displayName, this.uid, this.email, ArrayList())

fun <T: ViewModel> T.createFactory(): ViewModelProvider.Factory {
    val viewModel = this
    return object: ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T: ViewModel> create(modelClass: Class<T>): T = viewModel as T
    }
}