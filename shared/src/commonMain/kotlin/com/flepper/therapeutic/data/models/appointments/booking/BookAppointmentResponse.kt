package com.flepper.therapeutic.data.models.appointments.booking

import io.realm.kotlin.types.RealmObject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Data(@SerialName("booking") val booking: BookAppointmentResponse,@SerialName("errors") val errors:List<String>? = null)

@Serializable
data class BookAppointmentResponse(
    @SerialName("created_at") val createdAt: String = "",
    @SerialName("customer_note") val customerNote: String = "",
    @SerialName("source") val source: String = "",
    @SerialName("start_at") var startAt: String = "",
    @SerialName("transition_time_minutes") val transitionTimeMinutes: Int = 0,
    @SerialName("version") val version: Int = 0,
    @SerialName("location_id") val locationId: String = "",
    @SerialName("location_type") val locationType: String = "",
    @SerialName("appointment_segments") val appointmentSegments: List<AppointmentSegmentsItem>?,
    @SerialName("updated_at") val updatedAt: String = "",
    @SerialName("all_day") val allDay: Boolean = false,
    @SerialName("id") val id: String = "",
    @SerialName("customer_id") val customerId: String = "",
    @SerialName("status") val status: String = ""
) {
    fun toBookAppointmentResponseDao() = BookAppointmentResponseDao().also { dao ->
        dao.createdAt = createdAt
        dao.customerNote = customerNote
        dao.source = source
        dao.startAt = startAt
        dao.transitionTimeMinutes = transitionTimeMinutes
        dao.version = version
        dao.locationId = locationId
        dao.locationType = locationType
        dao.appointmentSegment = appointmentSegments?.first()?.toAppointmentSegmentItemDao()
        dao.updatedAt = updatedAt
        dao.allDay = allDay
        dao.id = id
        dao.customerId = customerId
        dao.status = status
    }
}

class BookAppointmentResponseDao : RealmObject {
    var createdAt: String = ""
    var customerNote: String = ""
    var source: String = ""
    var startAt: String = ""
    var transitionTimeMinutes: Int = 0
    var version: Int = 0
    var locationId: String = ""
    var locationType: String = ""
    var appointmentSegment: AppointmentSegmentItemDao? = null
    var updatedAt: String = ""
    var allDay: Boolean = false
    var id: String = ""
    var customerId: String = ""
    var status: String = ""

    fun toBookingResponse() = BookAppointmentResponse(
        createdAt,
        customerNote,
        source,
        startAt,
        transitionTimeMinutes,
        version,
        locationId,
        locationType,
        listOf(appointmentSegment?.toAppointmentSegment()!!),
        updatedAt,
        allDay,
        id,
        customerId,
        status
    )
}