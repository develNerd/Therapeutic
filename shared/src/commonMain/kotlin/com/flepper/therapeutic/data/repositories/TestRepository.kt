package com.flepper.therapeutic.data.repositories

import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.models.Auction
import com.flepper.therapeutic.data.network.ApiResult
import kotlinx.coroutines.flow.Flow



/*
*
*FlowResult is a type alias in  Contants.kt  Flow<ApiResult<T>>*/
interface TestRepository {
    suspend fun getTestDocsString():FlowResult<String>
    suspend fun getCode():FlowResult<String>
}