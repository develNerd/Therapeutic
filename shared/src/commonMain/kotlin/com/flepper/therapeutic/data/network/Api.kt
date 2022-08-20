package com.flepper.therapeutic.data.network

import com.flepper.therapeutic.data.models.Auction
import io.ktor.client.features.*
import io.ktor.client.request.*

class Api(private val ktorHttpClient: KtorHttpClient) {

    suspend fun getKtorDocs(): List<Auction> {
        return ktorHttpClient.GET("auctions_data/")
    }

    suspend fun getCode(): String {
        return ""
    }

}