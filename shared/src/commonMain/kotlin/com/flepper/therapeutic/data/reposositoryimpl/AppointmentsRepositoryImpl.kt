package com.flepper.therapeutic.data.reposositoryimpl

import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.models.Filter
import com.flepper.therapeutic.data.models.SquareSearchQuery
import com.flepper.therapeutic.data.models.customer.Customer
import com.flepper.therapeutic.data.models.customer.SearchCustomer
import com.flepper.therapeutic.data.network.Api
import com.flepper.therapeutic.data.network.makeRequestToApi
import com.flepper.therapeutic.data.repositories.AppointmentsRepository

class AppointmentsRepositoryImpl(private val api: Api) : AppointmentsRepository {
    override suspend fun createCustomer(request: Customer): FlowResult<Customer> = makeRequestToApi{
        api.createCustomer(request)
    }

    override suspend fun getCustomer(request: Filter): FlowResult<Customer> = makeRequestToApi {
        api.getCustomer(SearchCustomer(SquareSearchQuery(request)))
    }
}

