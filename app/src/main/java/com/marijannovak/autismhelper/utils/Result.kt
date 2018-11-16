package com.marijannovak.autismhelper.utils

sealed class Result<out T : Any?>

data class Success<out T: Any?>(val data: T): Result<T>()

data class Failure(val error: Throwable?): Result<Nothing>()

inline fun <T : Any?> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Success) action(data)
    return this
}

inline fun <T : Any?> Result<T>.onError(action: (Throwable) -> Unit) {
    if (this is Failure && error != null) action(error)
}