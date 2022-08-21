package com.flepper.therapeutic.data.models.appointments.availabletimeresponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentSegmentsItem(@SerialName("duration_minutes") val durationMinutes: Int = 0,
                                   @SerialName("team_member_id") val teamMemberId: String = "",
                                   @SerialName("service_variation_id") val serviceVariationVersion: Long = 0,
                                   @SerialName("service_variation_version") val serviceVariationId: String = "")