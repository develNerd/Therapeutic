package com.flepper.therapeutic.data.repositories

import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.models.Filter
import com.flepper.therapeutic.data.models.TeamMembersItem
import com.flepper.therapeutic.data.models.appointments.SearchAvailabilityRequest
import com.flepper.therapeutic.data.models.appointments.availabletimeresponse.AvailableTeamMemberTime
import com.flepper.therapeutic.data.models.appointments.booking.BookAppointmentResponse
import com.flepper.therapeutic.data.models.appointments.booking.BookingRequest
import com.flepper.therapeutic.data.models.customer.Customer
import com.flepper.therapeutic.data.models.customer.CustomerResponse
import com.flepper.therapeutic.data.reposositoryimpl.FlowList

interface AppointmentsRepository {
    suspend fun createCustomer(request:Customer):FlowResult<CustomerResponse>
    suspend fun getCustomer(request: Filter):FlowResult<List<CustomerResponse>>
    suspend fun getTeamMembers():FlowResult<List<TeamMembersItem>>
    suspend fun saveTeamMembersLocal(teamMembersItem: List<TeamMembersItem>)
    suspend fun getTeamMembersLocal():FlowList<TeamMembersItem>
    suspend fun getTeamAvailableTimes(request: SearchAvailabilityRequest):FlowResult<List<AvailableTeamMemberTime>>
    suspend fun bookAppointment(request: BookingRequest):FlowResult<BookAppointmentResponse>
    suspend fun saveBookingLocal(request: BookAppointmentResponse)
    suspend fun getAppointmentsLocal():FlowList<BookAppointmentResponse>
    suspend fun cancelBooking(bookingId:String):FlowResult<BookAppointmentResponse>
    suspend fun deleteBookingLocal()
}