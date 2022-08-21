package com.flepper.therapeutic.data.models.appointments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartAtRange(@SerialName("end_at") val endAt: String = "",
                        @SerialName("start_at") val startAt: String = "")