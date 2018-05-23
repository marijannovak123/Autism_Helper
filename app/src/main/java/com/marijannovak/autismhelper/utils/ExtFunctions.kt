package com.marijannovak.autismhelper.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.Fragment
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


fun FirebaseUser.mapToUser(singupRequest: SignupRequest) = User(this.uid, singupRequest.username, singupRequest.email, "", "", emptyMap(), null)

fun FirebaseUser.mapToUser() = User(this.uid, this.displayName, this.email, "", "", emptyMap(), null)

/**
 * factory for custom viewmodels with parameters in constructor
 */
fun <T : ViewModel> T.createFactory(): ViewModelProvider.Factory {
    val viewModel = this
    return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModel as T
    }
}

/**
 * check if viewmodelactivity to inject or not
 */
inline fun <reified T : Activity> T.isInjectable(): Boolean {
    return ViewModelActivity::class.java.isAssignableFrom(this.javaClass)
}

/**
 * check if should inject into fragment
 */
inline fun <reified T : Fragment> T.isInjectableFragment(): Boolean {
    return InjectableFragment::class.java.isAssignableFrom(this.javaClass)
}

inline fun <reified T : Any> T.logTag(): String {
    return T::class.java.simpleName
}

/**
 * use threads for RxJava
 */
fun <T> Single<T>.handleThreading(): Single<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun Completable.handleThreading(): Completable {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.handleThreading(): Flowable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Maybe<T>.handleThreading(): Maybe<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> io.reactivex.Observable<T>.handleThreading(): Observable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

/**
 * timestamp to date string
 */
@SuppressLint("SimpleDateFormat")
fun Long.toDayMonthString(): String {
    val dateFormat = SimpleDateFormat("dd/MM")
    val date = Date(this)
    return dateFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
fun Long.toDateString(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    val date = Date(this)
    return dateFormat.format(date)
}

/**
 * AAC list of phrases to sentence
 */
fun ArrayList<String>.toSentence(): String {
    val stringBuilder = StringBuilder()
    this.forEach {
        stringBuilder.append(it)
                .append("\t")
    }

    return stringBuilder.toString()
}

/**
 * generate a name for phrase from text provided
 */
fun String.replaceSpacesWithUnderscores(): String {
    return this.replace(" ", "_").toLowerCase()
}

/**
 * map values to list
 */
fun <K, V> Map<K, V>.mapToList(): List<V> {
    val list = ArrayList<V>()
    this.forEach{
        list += it.value
    }
    return list
}

/**
 * turn list to map to upload
 */
fun List<Child>.toMap(): Map<String,Child> {
    val map = HashMap<String,com.marijannovak.autismhelper.data.models.Child>()
    this.forEach {
        map += Pair(it.id, it)
    }
    return map
}