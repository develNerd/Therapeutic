package com.flepper.therapeutic.data.models.appointments.booking

import io.realm.kotlin.types.RealmObject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentSegmentsItem(
    @SerialName("duration_minutes") val durationMinutes: Int = 0,
    @SerialName("team_member_id") val teamMemberId: String = "",
    @SerialName("service_variation_version") val serviceVariationVersion: Long = 0,
    @SerialName("service_variation_id") val serviceVariationId: String = ""
) {
    fun toAppointmentSegmentItemDao() = AppointmentSegmentItemDao().also { dao ->
        dao.durationMinutes = durationMinutes
        dao.teamMemberId = teamMemberId
        dao.serviceVariationId = serviceVariationId
        dao.serviceVariationVersion = serviceVariationVersion

    }
}


class AppointmentSegmentItemDao : RealmObject {
    var durationMinutes: Int = 0
    var teamMemberId: String = ""
    var serviceVariationVersion: Long = 0L
    var serviceVariationId: String = ""

    fun toAppointmentSegment() = AppointmentSegmentsItem(
        durationMinutes,
        teamMemberId,
        serviceVariationVersion
    )
}