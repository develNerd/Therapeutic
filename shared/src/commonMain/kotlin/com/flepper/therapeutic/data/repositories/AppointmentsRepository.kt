package com.flepper.therapeutic.data.repositories

import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.models.Filter
import com.flepper.therapeutic.data.models.customer.Customer

interface AppointmentsRepository {
    suspend fun createCustomer(request:Customer):FlowResult<Customer>
    suspend fun getCustomer(request: Filter):FlowResult<Customer>
}