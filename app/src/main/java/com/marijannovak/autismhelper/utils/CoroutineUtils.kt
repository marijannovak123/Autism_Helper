package com.marijannovak.autismhelper.utils

import io.reactivex.Flowable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.reactive.openSubscription
import retrofit2.Response


/**
 * Helpers for MVVM + Repository + DatSource/Service architecture with Kotlin Coroutines
 * by Marijan Novak
 */


/**
 * Result to return to ViewModel to handle results, holds data or error
 */
sealed class LoadResult<out T : Any?> {

    companion object {
        /**
         * Create load data call, catching any exceptions thrown.
         * If call is successful return the result, otherwise failure.
         */
        inline fun <T> create(block: () -> T): LoadResult<T> {
            return try {
                val result = block()
                Success(result)
            } catch (e: Exception) {
                Failure(e)
            }
        }

        /**
         * Try refreshing data in local storage by fetching from remote source in refreshBlock, load
         * local data regardless it was refreshed or not
         */
        inline fun <T> refreshAndLoadLocalData(refreshBlock: () -> Unit, loadBlock: () -> T): LoadResult<T> {
            try {
                refreshBlock()
            } catch (e: Exception) {
                //ignore exception, load data already existing locally
            }

            return create {
                loadBlock()
            }
        }
    }
}

/**
 * Represents successful data load and returns the data
 */
data class Success<out T : Any?>(val data: T) : LoadResult<T>()

/**
 * Represents failure in loading data, return the exception thrown
 */
data class Failure(val error: Throwable?) : LoadResult<Nothing>()

/**
 * Handle successful call. Invoke action block passed. Return same result
 * to further use it for onError
 */
inline fun <T : Any?> LoadResult<T>.onSuccess(action: (T) -> Unit): LoadResult<T> {
    if (this is Success) action(data)
    return this
}

/**
 * Handle failure loading result and invoke the action passed. Return the
 * result to further use it for onSuccess etc.
 */
inline fun <T : Any?> LoadResult<T>.onError(action: (Throwable) -> Unit): LoadResult<T> {
    if (this is Failure && error != null) action(error)
    return this
}

/**
 * Represents completable call that returns no data. If exception thrown return it, otherwise null
 */
class Completion(val error: Throwable? = null) {
    companion object {
        /**
         * Call passed block and return Completion result
         */
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

/**
 * Handle Completion result
 */
inline fun Completion.onCompletion(action: (Throwable?) -> Unit) { action(error) }



/**
 * Create calls passed to Repository from DataSource and Service
 */
class CoroutineHelper {
    companion object {
        /**
         * Make an async call on preferred dispatcher
         */
        suspend fun <T> deferredCall(
                dispatcher: CoroutineDispatcher = Dispatchers.IO,
                call: () -> T
        ): T {
            return withContext(dispatcher) {
                 call()
            }
        }

        /**
         * Return a ReceiveChannel from a Flowable (used for Flowable returned from Room DB)
         */
        suspend fun <T> openFlowableChannel(
                dispatcher: CoroutineDispatcher = Dispatchers.IO,
                call: () -> Flowable<T>
        ): ReceiveChannel<T> {
            return withContext(dispatcher) {
                call().distinctUntilChanged().openSubscription()
            }
        }

        /**
         * Await a Deferred on a preferred Dispatcher
         */
        suspend fun <T> awaitDeferred(
                dispatcher: CoroutineDispatcher = Dispatchers.IO,
                call: () -> Deferred<T>
        ): T {
            return withContext(dispatcher) {
                call().await()
            }
        }

        /**
         * Await a Deferred with a Retrofit Response and return the body if successful
         */
        suspend fun <T> awaitDeferredResponse(
                dispatcher: CoroutineDispatcher = Dispatchers.IO,
                block: () -> Deferred<Response<T?>>
        ): T? {
            return withContext(dispatcher) {
                block().await().body()
            }
        }

        /**
         * Combine T1 and T2 calls results, pass combineBlock to implement combination class R
         */
        suspend inline fun <T1, T2, R> combineResult(
                call1: Deferred<T1>,
                call2: Deferred<T2>,
                combineBlock: (T1, T2) -> R
        ): R {
            val result1 = call1.await()
            val result2 = call2.await()
            return combineBlock(result1, result2)
        }

        /**
         * Analogous to the previous one, but combining three calls
         */
        suspend inline fun <T1, T2, T3, R> combineResult(
                call1: Deferred<T1>,
                call2: Deferred<T2>,
                call3: Deferred<T3>,
                combineBlock: (T1, T2, T3) -> R
        ): R {
            val result1 = call1.await()
            val result2 = call2.await()
            val result3 = call3.await()
            return combineBlock(result1, result2, result3)
        }

        /**
         * Analogous to the previous one, but combining four calls
         */
        suspend inline fun <T1, T2, T3, T4, R> combineResult(
                call1: Deferred<T1>,
                call2: Deferred<T2>,
                call3: Deferred<T3>,
                call4: Deferred<T4>,
                combineBlock: (T1, T2, T3, T4) -> R
        ): R {
            val result1 = call1.await()
            val result2 = call2.await()
            val result3 = call3.await()
            val result4 = call4.await()
            return combineBlock(result1, result2, result3, result4)
        }

        /**
         * Execute multiple Completion calls in parallel
         */
        suspend inline fun executeMultiple(
                dispatcher: CoroutineDispatcher = Dispatchers.IO,
                vararg calls: Deferred<Any>
        ): List<Any> {
            return withContext(dispatcher) {
                calls.asList().awaitAll()
            }
        }

    }
}


