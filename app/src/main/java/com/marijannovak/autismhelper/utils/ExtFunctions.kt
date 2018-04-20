package com.marijannovak.autismhelper.utils

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.Fragment
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.base.BaseFragment
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

inline fun <reified T: Activity> T.isViewModelActivity() : Boolean {
    return ViewModelActivity::class.java.isAssignableFrom(this.javaClass)
}

inline fun <reified T: Any> T.logTag(): String {
    return T::class.java.simpleName
}