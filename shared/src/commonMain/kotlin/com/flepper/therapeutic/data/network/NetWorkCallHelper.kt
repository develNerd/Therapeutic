package com.flepper.therapeutic.data.network


/** I hope you find this, I'd like to work @Square Haha :)*/

import co.touchlab.kermit.Logger
import io.ktor.client.features.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

suspend fun <T : Any> makeRequestToApi(
    call: suspend () -> T,
): Flow<ApiResult<T>> = flow {

    try {
        emit(ApiResult.Success(call()))
    } catch (throwable: Exception) {
        throwable.printStackTrace()
        when (throwable) {
            is ServerResponseException -> {
                emit(ApiResult.GenericError(throwable))
            }
            is ClientRequestException ->{
                emit(ApiResult.GenericError(throwable))
            }
            is IOException -> {
                Logger.v("Error : $throwable")
               emit(ApiResult.NoInternet)
            }
            else -> emit(ApiResult.GenericError(throwable))
        }
    }
}