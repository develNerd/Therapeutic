package com.flepper.therapeutic.data.models.appointments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchAvailabilityRequest(@SerialName("query") val query: AvailabilityQuery)