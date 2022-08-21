package com.flepper.therapeutic.data.network

import com.flepper.therapeutic.data.models.SquareSearchQuery
import com.flepper.therapeutic.data.models.TeamMembersItem
import com.flepper.therapeutic.data.models.appointments.SearchAvailabilityRequest
import com.flepper.therapeutic.data.models.appointments.availabletimeresponse.AvailableTeamMemberTime
import com.flepper.therapeutic.data.models.customer.Customer
import com.flepper.therapeutic.data.models.customer.CustomerResponse
import com.flepper.therapeutic.data.models.customer.SearchCustomer

class Api(private val squareHttpClient: SquareHttpClient) {

    suspend fun createCustomer(request: Customer): CustomerResponse {
        return squareHttpClient.POST(CUSTOMERS,request)
    }

    suspend fun getCustomer(request: SearchCustomer): CustomerResponse {
        return squareHttpClient.POST(CUSTOMERS_SEARCH,request)
    }

    suspend fun getTeamMembers():List<TeamMembersItem>{
        return squareHttpClient.POST(TEAM_MEMBER_SEARCH,null)
    }

    suspend fun getTeamMembersAvailableTimes(request: SearchAvailabilityRequest):List<AvailableTeamMemberTime>{
        return squareHttpClient.POST(SEARCH_AVAILABILITY,request)
    }

}