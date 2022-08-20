package com.flepper.therapeutic.data.network

import io.ktor.client.features.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ReqData<T>(
    @SerialName("data")
    val data: T?,
)
@Serializable
data class StatusMessage(
    @SerialName("status_msg")
    val statusMessage: List<String>,
)

typealias ErrorResponse = ReqData<StatusMessage>

sealed class ApiResult<out T : Any> {

    data class Success<out T : Any>(val response: T) : ApiResult<T>()


    data class GenericError(val error: Exception) : ApiResult<Nothing>()

    data class HttpError(val error: ClientRequestException) : ApiResult<Nothing>()

    object InProgress : ApiResult<Nothing>()

    object NoInternet : ApiResult<Nothing>()

    // Override for quick logging
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success [data=$response]"
            is HttpError -> "Http Error [httpCode=${error.message}]"
            is GenericError -> "Error [error=${error.message}]"
            is NoInternet -> "No Internet"
            is InProgress -> "In progress"
        }
    }

    fun toShortString(): String {
        return when (this) {
            is Success<*> -> "Success [data=$response]"
            is HttpError -> "Http Error [httpCode=${error.message}]"
            is GenericError -> "Error [error=${error.message}]"
            is NoInternet -> "No Internet"
            is InProgress -> "In progress"
        }
    }
}
