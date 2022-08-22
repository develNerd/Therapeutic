package com.flepper.therapeutic.data.usecasefactories

import com.flepper.therapeutic.data.repositories.AppointmentsRepository
import com.flepper.therapeutic.data.usecases.*
import kotlinx.coroutines.CoroutineScope

class AppointmentsUseCaseFactory(coroutineScope: CoroutineScope,appointmentsRepository: AppointmentsRepository) {
    val createCustomerUseCase = CreateCustomerUseCase(coroutineScope, appointmentsRepository)
    val getCustomerUseCase = GetCustomerUseCase(coroutineScope, appointmentsRepository)
    val getTeamMembersUseCase = GetTeamMembersUseCase(coroutineScope, appointmentsRepository)
    val saveTeamMemberLocalUseCase = SaveTeamMemberLocalUseCase(coroutineScope, appointmentsRepository)
    val getTeamMembersLocalUseCase = GetTeamMembersLocalUseCase(coroutineScope, appointmentsRepository)
    val getAvailableTimeUseCase = GetAvailableTimeUseCase(coroutineScope, appointmentsRepository)
    val bookAppointmentUseCase = BookAppointmentUseCase(coroutineScope, appointmentsRepository)
    val saveBookingLocal = SaveBookingLocalUseCase(coroutineScope, appointmentsRepository)
    val getBookingLocalUseCase = GetBookingLocalUseCase(coroutineScope, appointmentsRepository)
    val cancelBookingUseCase = CancelBookingUseCase(coroutineScope, appointmentsRepository)
    val cancelBookingLocalUseCase = CancelBookingLocalUseCase(coroutineScope, appointmentsRepository)

}