package com.flepper.therapeutic.data.models.appointments.availabletimeresponse

import kotlinx.datetime.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableTeamMemberTime(
    @SerialName("appointment_segments") val appointmentSegments: List<AppointmentSegmentsItem>?,
    @SerialName("start_at") val startAt: String = "",
    @SerialName("location_id") val locationId: String = ""
) {
    /** We need to convert start to the Users timezone. For the default account business time is in UTC, but I'm in GMT atm :) */
    fun convertStartToUserTimeZone(): Long {
        val startTime = Instant.parse(startAt)
        return startTime.toEpochMilliseconds()
    }
}



@Serializable
data class AvailableTimeResponse(@SerialName("availabilities") val availableTeamMemberTime:List<AvailableTeamMemberTime>,@SerialName("errors") val errors:List<String>? =null)