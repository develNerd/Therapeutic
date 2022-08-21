package com.flepper.therapeutic.data.models.appointments.availabletimeresponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableTeamMemberTime(@SerialName("appointment_segments") val appointmentSegments: List<AppointmentSegmentsItem>?,
                                   @SerialName("start_at") val startAt: String = "",
                                   @SerialName("location_id") val locationId: String = "")