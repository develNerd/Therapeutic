package com.flepper.therapeutic.data.usecases

import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.models.customer.Customer
import com.flepper.therapeutic.data.models.customer.CustomerResponse
import com.flepper.therapeutic.data.repositories.AppointmentsRepository
import com.flepper.therapeutic.data.repositories.TestRepository
import kotlinx.coroutines.CoroutineScope

class CreateCustomerUseCase(coroutineScope: CoroutineScope, private val appointmentsRepository: AppointmentsRepository) :
    BaseUseCaseDispatcher<Customer, FlowResult<CustomerResponse>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: Customer,
        coroutineScope: CoroutineScope
    ) = appointmentsRepository.createCustomer(request)
}