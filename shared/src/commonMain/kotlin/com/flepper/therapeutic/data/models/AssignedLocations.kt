package com.flepper.therapeutic.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssignedLocations (@SerialName("location_ids") val locationIds: List<String>? = null,
                              @SerialName("assignment_type") val assignmentType: String = "")