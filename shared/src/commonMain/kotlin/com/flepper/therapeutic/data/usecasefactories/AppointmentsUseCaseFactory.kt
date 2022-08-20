package com.flepper.therapeutic.data.usecasefactories

import com.flepper.therapeutic.data.repositories.AppointmentsRepository
import com.flepper.therapeutic.data.usecases.CreateCustomerUseCase
import com.flepper.therapeutic.data.usecases.GetCustomerUseCase
import kotlinx.coroutines.CoroutineScope

class AppointmentsUseCaseFactory(coroutineScope: CoroutineScope,appointmentsRepository: AppointmentsRepository) {
    val createCustomerUseCase = CreateCustomerUseCase(coroutineScope, appointmentsRepository)
    val getCustomerUseCase = GetCustomerUseCase(coroutineScope, appointmentsRepository)

}