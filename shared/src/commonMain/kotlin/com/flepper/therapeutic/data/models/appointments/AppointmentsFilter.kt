package com.flepper.therapeutic.data.models.appointments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentsFilter(@SerialName("booking_id") val bookingId: String = "",
                              @SerialName("start_at_range") val startAtRange: StartAtRange,
                              @SerialName("segment_filters") val segmentFilters: List<SegmentFiltersItem>?,
                              @SerialName("location_id") val locationId: String = "")