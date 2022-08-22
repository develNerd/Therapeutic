package com.flepper.therapeutic.data.models.appointments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailabilityQuery(@SerialName("filter") val filter: AppointmentsFilter)