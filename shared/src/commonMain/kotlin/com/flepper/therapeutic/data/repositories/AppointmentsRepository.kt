package com.flepper.therapeutic.data.repositories

import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.models.Filter
import com.flepper.therapeutic.data.models.TeamMembersItem
import com.flepper.therapeutic.data.models.appointments.SearchAvailabilityRequest
import com.flepper.therapeutic.data.models.appointments.availabletimeresponse.AvailableTeamMemberTime
import com.flepper.therapeutic.data.models.customer.Customer
import com.flepper.therapeutic.data.models.customer.CustomerResponse
import com.flepper.therapeutic.data.reposositoryimpl.FlowList

interface AppointmentsRepository {
    suspend fun createCustomer(request:Customer):FlowResult<CustomerResponse>
    suspend fun getCustomer(request: Filter):FlowResult<CustomerResponse>
    suspend fun getTeamMembers():FlowResult<List<TeamMembersItem>>
    suspend fun saveTeamMembersLocal(teamMembersItem: List<TeamMembersItem>)
    suspend fun getTeamMembersLocal():FlowList<TeamMembersItem>
    suspend fun getTeamAvailableTimes(request: SearchAvailabilityRequest):FlowResult<List<AvailableTeamMemberTime>>
}