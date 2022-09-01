package com.flepper.therapeutic.data.network

import com.flepper.therapeutic.data.models.SquareSearchQuery
import com.flepper.therapeutic.data.models.TeamMemberData
import com.flepper.therapeutic.data.models.TeamMembersItem
import com.flepper.therapeutic.data.models.appointments.SearchAvailabilityRequest
import com.flepper.therapeutic.data.models.appointments.availabletimeresponse.AvailableTeamMemberTime
import com.flepper.therapeutic.data.models.appointments.availabletimeresponse.AvailableTimeResponse
import com.flepper.therapeutic.data.models.appointments.booking.BookAppointmentResponse
import com.flepper.therapeutic.data.models.appointments.booking.BookingRequest
import com.flepper.therapeutic.data.models.appointments.booking.Data
import com.flepper.therapeutic.data.models.customer.*

class Api(private val squareHttpClient: SquareHttpClient) {

    suspend fun createCustomer(request: Customer): CustomerWrapperResponse {
        return squareHttpClient.POST(CUSTOMERS,request)
    }

    suspend fun getCustomer(request: SearchCustomer): GetCustomersResponse {
        return squareHttpClient.POST(CUSTOMERS_SEARCH,request)
    }

    suspend fun getTeamMembers(): TeamMemberData {
        return squareHttpClient.POST(TEAM_MEMBER_SEARCH,null)
    }

    suspend fun getTeamMembersAvailableTimes(request: SearchAvailabilityRequest): AvailableTimeResponse {
        return squareHttpClient.POST(SEARCH_AVAILABILITY,request)
    }

    suspend fun bookAppointment(request: BookingRequest): Data {
        return squareHttpClient.POST(BOOK_APPOINTMENT,request)
    }


    suspend fun cancelAppointment(bookingId:String):Data{
        return squareHttpClient.POST(cancelAppointmentUrl(bookingId),null)
    }


}