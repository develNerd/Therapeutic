package com.flepper.therapeutic.data.repositories

import com.flepper.therapeutic.data.network.Api
import com.flepper.therapeutic.data.network.ApiResult
import com.flepper.therapeutic.data.network.makeRequestToApi

class TestRepositoryImpl(private val api: Api) : TestRepository {


    override suspend fun getTestDocsString() = makeRequestToApi {
        ""
    }

    override suspend fun getCode() = makeRequestToApi {
        ""
    }

}