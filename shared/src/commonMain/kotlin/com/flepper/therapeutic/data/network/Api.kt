package com.flepper.therapeutic.data.network

import com.flepper.therapeutic.data.models.SquareSearchQuery
import com.flepper.therapeutic.data.models.customer.Customer
import com.flepper.therapeutic.data.models.customer.SearchCustomer

class Api(private val squareHttpClient: SquareHttpClient) {

    suspend fun createCustomer(request: Customer): Customer {
        return squareHttpClient.POST(CUSTOMERS,request)
    }

    suspend fun getCustomer(request: SearchCustomer): Customer {
        return squareHttpClient.POST(CUSTOMERS_SEARCH,request)
    }

}