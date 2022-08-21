package com.flepper.therapeutic.data.models.appointments.booking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Booking(@SerialName("appointment_segments") val appointmentSegments: List<AppointmentSegmentsItem>?,
                   @SerialName("customer_note") val customerNote: String = "",
                   @SerialName("customer_id") val customerId: String = "",
                   @SerialName("start_at") val startAt: String = "",
                   @SerialName("location_type") val locationType: String = "",
                   @SerialName("location_id") val locationId: String = "")