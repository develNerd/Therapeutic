package com.flepper.therapeutic.data.reposositoryimpl

import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.TherapeuticDb
import com.flepper.therapeutic.data.models.*
import com.flepper.therapeutic.data.models.appointments.SearchAvailabilityRequest
import com.flepper.therapeutic.data.models.appointments.availabletimeresponse.AvailableTeamMemberTime
import com.flepper.therapeutic.data.models.appointments.booking.BookAppointmentResponse
import com.flepper.therapeutic.data.models.appointments.booking.BookAppointmentResponseDao
import com.flepper.therapeutic.data.models.appointments.booking.BookingRequest
import com.flepper.therapeutic.data.models.customer.Customer
import com.flepper.therapeutic.data.models.customer.CustomerResponse
import com.flepper.therapeutic.data.models.customer.SearchCustomer
import com.flepper.therapeutic.data.network.Api
import com.flepper.therapeutic.data.network.makeRequestToApi
import com.flepper.therapeutic.data.repositories.AppointmentsRepository
import io.realm.kotlin.BaseRealm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.map

class AppointmentsRepositoryImpl(private val api: Api, therapeuticDb: TherapeuticDb) :
    AppointmentsRepository {


    private val db = therapeuticDb.invoke()

    override suspend fun createCustomer(request: Customer): FlowResult<CustomerResponse> =
        makeRequestToApi {
            api.createCustomer(request).customer
        }

    override suspend fun getCustomer(request: Filter): FlowResult<List<CustomerResponse>> =
        makeRequestToApi {
            api.getCustomer(SearchCustomer(SquareSearchQuery(request))).customers
        }

    override suspend fun getTeamMembers(): FlowResult<List<TeamMembersItem>> = makeRequestToApi {
        api.getTeamMembers().teamMembers
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
        return db.query<TeamMembersItemDao>().asFlow()
            .map { it.list.map { item -> item.toTeamMember() } }
    }

    override suspend fun getTeamAvailableTimes(request: SearchAvailabilityRequest): FlowResult<List<AvailableTeamMemberTime>> =
        makeRequestToApi {
            api.getTeamMembersAvailableTimes(request).availableTeamMemberTime
        }

    override suspend fun bookAppointment(request: BookingRequest): FlowResult<BookAppointmentResponse> =
        makeRequestToApi {
            api.bookAppointment(request).booking
        }

    override suspend fun saveBookingLocal(request: BookAppointmentResponse) {
        db.write {
            copyToRealm(request.toBookAppointmentResponseDao().apply {
                this.appointmentSegment =
                    request.appointmentSegments?.first()?.toAppointmentSegmentItemDao()
            }, UpdatePolicy.ALL)
        }
    }

    override suspend fun getAppointmentsLocal(): FlowList<BookAppointmentResponse> {
        return db.query<BookAppointmentResponseDao>().asFlow()
            .map { it.list.map { item -> item.toBookingResponse() } }
    }

    override suspend fun cancelBooking(bookingId: String):  FlowResult<BookAppointmentResponse> = makeRequestToApi{
        api.cancelAppointment(bookingId).booking
    }

    override suspend fun deleteBookingLocal() {

        db.writeBlocking {
            // Selected by a query
            val query = this.query<BookAppointmentResponseDao>()
            delete(query)

            // From a query result
            val results = query.find()
            delete(results)

            // From individual objects
            results.forEach { delete(it) }
        }
    }


}

