package com.flepper.therapeutic.data.models.appointments.booking

import io.realm.kotlin.types.RealmObject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentSegmentsItem(
    @SerialName("duration_minutes") val durationMinutes: Int = 0,
    @SerialName("team_member_id") val teamMemberId: String = "",
    @SerialName("any_team_member") val anyTeamMember: Boolean = false,
    @SerialName("intermission_minutes") val intermissionMinutes: Int = 0,
    @SerialName("service_variation_version") val serviceVariationVersion: Long = 0,
    @SerialName("service_variation_id") val serviceVariationId: String = ""
) {
    fun toAppointmentSegmentItemDao() = AppointmentSegmentItemDao().also { dao ->
        dao.durationMinutes = durationMinutes
        dao.teamMemberId = teamMemberId
        dao.anyTeamMember = anyTeamMember
        dao.intermissionMinutes = intermissionMinutes
        dao.serviceVariationId = serviceVariationId
        dao.serviceVariationVersion = serviceVariationVersion

    }
}


class AppointmentSegmentItemDao : RealmObject {
    var durationMinutes: Int = 0
    var teamMemberId: String = ""
    var anyTeamMember: Boolean = false
    var intermissionMinutes: Int = 0
    var serviceVariationVersion: Long = 0L
    var serviceVariationId: String = ""
}