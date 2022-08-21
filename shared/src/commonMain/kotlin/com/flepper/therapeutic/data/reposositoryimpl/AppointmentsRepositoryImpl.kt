package com.flepper.therapeutic.data.reposositoryimpl

import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.TherapeuticDb
import com.flepper.therapeutic.data.models.*
import com.flepper.therapeutic.data.models.appointments.SearchAvailabilityRequest
import com.flepper.therapeutic.data.models.appointments.availabletimeresponse.AvailableTeamMemberTime
import com.flepper.therapeutic.data.models.customer.Customer
import com.flepper.therapeutic.data.models.customer.CustomerResponse
import com.flepper.therapeutic.data.models.customer.SearchCustomer
import com.flepper.therapeutic.data.network.Api
import com.flepper.therapeutic.data.network.makeRequestToApi
import com.flepper.therapeutic.data.repositories.AppointmentsRepository
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.map

class AppointmentsRepositoryImpl(private val api: Api, therapeuticDb: TherapeuticDb) : AppointmentsRepository {


    private val db = therapeuticDb.invoke()

    override suspend fun createCustomer(request: Customer): FlowResult<CustomerResponse> = makeRequestToApi{
        api.createCustomer(request)
    }

    override suspend fun getCustomer(request: Filter): FlowResult<CustomerResponse> = makeRequestToApi {
        api.getCustomer(SearchCustomer(SquareSearchQuery(request)))
    }

    override suspend fun getTeamMembers(): FlowResult<List<TeamMembersItem>> = makeRequestToApi {
        api.getTeamMembers()
    }

    /** Should save a max of 4 items */
    override suspend fun saveTeamMembersLocal(teamMembersItem: List<TeamMembersItem>) {
        db.write {
            teamMembersItem.map { it.getDao() }.forEach {
                copyToRealm(it, UpdatePolicy.ALL)
            }
        }
    }

    /** Should retun o= max of 4 team members*/
    override suspend fun getTeamMembersLocal(): FlowList<TeamMembersItem> {
        return db.query<TeamMembersItemDao>().asFlow().map { it.list.map {item -> item.toTeamMember() } }
    }

    override suspend fun getTeamAvailableTimes(request: SearchAvailabilityRequest): FlowResult<List<AvailableTeamMemberTime>> = makeRequestToApi {
        api.getTeamMembersAvailableTimes(request)
    }


}

