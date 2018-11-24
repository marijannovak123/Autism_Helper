package com.marijannovak.autismhelper.utils

import kotlin.Exception

sealed class LoadResult<out T : Any?> {
    companion object {
        inline fun <T> create(block: () -> T): LoadResult<T> {
            return try {
                val result = block()
                Success(result)
            } catch (e: Exception) {
                Failure(e)
            }
        }
    }
}

class Completion(val error: Throwable? = null) {
    companion object {
        inline fun create(block: () -> Unit): Completion {
            return try {
                block.invoke()
                Completion()
            } catch (e: Exception) {
                Completion(e)
            }
        }
    }
}

data class Success<out T: Any?>(val data: T): LoadResult<T>()

data class Failure(val error: Throwable?): LoadResult<Nothing>()

inline fun <T : Any?> LoadResult<T>.onSuccess(action: (T) -> Unit): LoadResult<T> {
    if (this is Success) action(data)
    return this
}

inline fun <T : Any?> LoadResult<T>.onError(action: (Throwable) -> Unit) {
    if (this is Failure && error != null) action(error)
}

inline fun Completion.onCompletion(action: (Throwable?) -> Unit) { action(error) }

