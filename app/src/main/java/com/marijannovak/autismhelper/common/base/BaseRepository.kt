package com.marijannovak.autismhelper.common.base

import com.marijannovak.autismhelper.utils.Failure
import com.marijannovak.autismhelper.utils.Success
import com.marijannovak.autismhelper.utils.LoadResult
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class BaseRepository {

    suspend fun <M : Any> getRemoteData(call: Deferred<Response<M>>, logicBlock: suspend (M) -> LoadResult<M>): LoadResult<M> {
        return try {
            val response = call.await()
            if(response.isSuccessful) {
                if(response.body() != null) {
                    logicBlock(response.body()!!)
                } else {
                    Failure(KotlinNullPointerException())
                }
            } else {
                Failure(Throwable(response.message()))
            }
        } catch (e: Throwable) {
            Failure(e)
        }
    }

    suspend fun <M: Any> getRemoteDataList(apiCall: Deferred<Response<List<M>>>, logicBlock: suspend (List<M>) -> LoadResult<List<M>>): LoadResult<List<M>> {
        return withContext(Dispatchers.IO) {
            try {
                val loadedData = apiCall.await()
                if(loadedData.isSuccessful && loadedData.body() != null) {
                    logicBlock(loadedData.body()!!)
                } else {
                    Failure(KotlinNullPointerException())
                }
            } catch (e: Throwable) {
                Failure(e)
            }
        }
    }

    suspend fun <M: Any> getLocalData(dbCall: Deferred<M>, logicBlock: suspend (M) -> LoadResult<M>): LoadResult<M> {
        return withContext(Dispatchers.Default) {
            try {
                val loadedData = dbCall.await()
                if(loadedData != null) {
                    logicBlock(loadedData)
                } else {
                    Failure(KotlinNullPointerException())
                }
            } catch (e: Throwable) {
                Failure(e)
            }
        }
    }

    suspend fun completableNetworkCall(call: Deferred<Response<Nothing>>): LoadResult<Boolean> {
        return try {
            val result = call.await()
            if(result.isSuccessful) {
                Success(true)
            } else {
                Failure(Throwable(result.message()))
            }
        } catch (e: Throwable) {
            Failure(e)
        }
    }

}
