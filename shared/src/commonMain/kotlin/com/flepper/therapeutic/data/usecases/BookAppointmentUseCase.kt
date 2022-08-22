package com.flepper.therapeutic.data.usecases

import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.models.TeamMembersItem
import com.flepper.therapeutic.data.models.appointments.SearchAvailabilityRequest
import com.flepper.therapeutic.data.models.appointments.availabletimeresponse.AvailableTeamMemberTime
import com.flepper.therapeutic.data.models.appointments.booking.BookAppointmentResponse
import com.flepper.therapeutic.data.models.appointments.booking.BookingRequest
import com.flepper.therapeutic.data.repositories.AppointmentsRepository
import com.flepper.therapeutic.data.reposositoryimpl.FlowList
import kotlinx.coroutines.CoroutineScope

class BookAppointmentUseCase(coroutineScope: CoroutineScope, private val appointmentsRepository: AppointmentsRepository) :
    BaseUseCaseDispatcher<BookingRequest, FlowResult<BookAppointmentResponse>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: BookingRequest,
        coroutineScope: CoroutineScope
    ) = appointmentsRepository.bookAppointment(request)
}

class SaveBookingLocalUseCase(coroutineScope: CoroutineScope,private val appointmentsRepository: AppointmentsRepository) :
    BaseUseCaseDispatcher<BookAppointmentResponse, Unit>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: BookAppointmentResponse,
        coroutineScope: CoroutineScope
    ) = appointmentsRepository.saveBookingLocal(request)

}

class GetBookingLocalUseCase(coroutineScope: CoroutineScope,private val appointmentsRepository: AppointmentsRepository) :
    BaseUseCaseDispatcher<Unit, FlowList<BookAppointmentResponse>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: Unit,
        coroutineScope: CoroutineScope
    ) = appointmentsRepository.getAppointmentsLocal()

}

class CancelBookingUseCase(coroutineScope: CoroutineScope, private val appointmentsRepository: AppointmentsRepository) :
    BaseUseCaseDispatcher<String, FlowResult<BookAppointmentResponse>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: String,
        coroutineScope: CoroutineScope
    ) = appointmentsRepository.cancelBooking(request)
}

class CancelBookingLocalUseCase(coroutineScope: CoroutineScope, private val appointmentsRepository: AppointmentsRepository) :
    BaseUseCaseDispatcher<Unit, Unit>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: Unit,
        coroutineScope: CoroutineScope
    ) = appointmentsRepository.deleteBookingLocal()
}

