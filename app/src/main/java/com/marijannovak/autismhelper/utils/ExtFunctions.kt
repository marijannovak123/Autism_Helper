package com.marijannovak.autismhelper.utils

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


fun FirebaseUser.mapToUser(singupRequest: SignupRequest)
        = User(singupRequest.username, this.uid, singupRequest.email, "", emptyList(), emptyList())

fun FirebaseUser.mapToUser() = User(this.uid, this.displayName, this.email, "", emptyList(), emptyList())

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

fun <T> Single<T>.handleThreading() : Single<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun Completable.handleThreading() : Completable {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.handleThreading(): Flowable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Maybe<T>.handleThreading(): Maybe<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}