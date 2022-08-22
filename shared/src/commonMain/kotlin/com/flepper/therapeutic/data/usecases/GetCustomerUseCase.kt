package com.flepper.therapeutic.data.usecases

import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.models.Filter
import com.flepper.therapeutic.data.models.customer.Customer
import com.flepper.therapeutic.data.models.customer.CustomerResponse
import com.flepper.therapeutic.data.repositories.AppointmentsRepository
import kotlinx.coroutines.CoroutineScope

class GetCustomerUseCase(coroutineScope: CoroutineScope, private val appointmentsRepository: AppointmentsRepository) :
    BaseUseCaseDispatcher<Filter, FlowResult<List<CustomerResponse>>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: Filter,
        coroutineScope: CoroutineScope
    ) = appointmentsRepository.getCustomer(request)
}