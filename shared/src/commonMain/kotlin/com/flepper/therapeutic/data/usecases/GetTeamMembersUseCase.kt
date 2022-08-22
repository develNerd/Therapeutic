package com.flepper.therapeutic.data.usecases

import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.models.Filter
import com.flepper.therapeutic.data.models.TeamMembersItem
import com.flepper.therapeutic.data.models.WorldWideEvent
import com.flepper.therapeutic.data.models.appointments.SearchAvailabilityRequest
import com.flepper.therapeutic.data.models.appointments.availabletimeresponse.AvailableTeamMemberTime
import com.flepper.therapeutic.data.models.customer.CustomerResponse
import com.flepper.therapeutic.data.repositories.AppointmentsRepository
import com.flepper.therapeutic.data.repositories.EventsRepository
import com.flepper.therapeutic.data.reposositoryimpl.FlowList
import kotlinx.coroutines.CoroutineScope

class GetTeamMembersUseCase(coroutineScope: CoroutineScope, private val appointmentsRepository: AppointmentsRepository) :
    BaseUseCaseDispatcher<Unit, FlowResult<List<TeamMembersItem>>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: Unit,
        coroutineScope: CoroutineScope
    ) = appointmentsRepository.getTeamMembers()
}

class SaveTeamMemberLocalUseCase(coroutineScope: CoroutineScope,private val appointmentsRepository: AppointmentsRepository) :
    BaseUseCaseDispatcher<List<TeamMembersItem>, Unit>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: List<TeamMembersItem>,
        coroutineScope: CoroutineScope
    ) = appointmentsRepository.saveTeamMembersLocal(request)

}


class GetTeamMembersLocalUseCase(coroutineScope: CoroutineScope,private val appointmentsRepository: AppointmentsRepository) :
    BaseUseCaseDispatcher<Unit, FlowList<TeamMembersItem>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: Unit,
        coroutineScope: CoroutineScope
    ) = appointmentsRepository.getTeamMembersLocal()

}

class GetAvailableTimeUseCase(coroutineScope: CoroutineScope, private val appointmentsRepository: AppointmentsRepository) :
    BaseUseCaseDispatcher<SearchAvailabilityRequest, FlowResult<List<AvailableTeamMemberTime>>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: SearchAvailabilityRequest,
        coroutineScope: CoroutineScope
    ) = appointmentsRepository.getTeamAvailableTimes(request)
}
