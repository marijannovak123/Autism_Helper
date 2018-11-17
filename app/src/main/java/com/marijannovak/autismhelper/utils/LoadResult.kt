package com.marijannovak.autismhelper.utils

sealed class LoadResult<out T : Any?>

data class Success<out T: Any?>(val data: T): LoadResult<T>()

data class Failure(val error: Throwable?): LoadResult<Nothing>()

inline fun <T : Any?> LoadResult<T>.onSuccess(action: (T) -> Unit): LoadResult<T> {
    if (this is Success) action(data)
    return this
}

inline fun <T : Any?> LoadResult<T>.onError(action: (Throwable) -> Unit) {
    if (this is Failure && error != null) action(error)
}