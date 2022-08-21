package com.flepper.therapeutic.data.models.appointments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SegmentFiltersItem(@SerialName("team_member_id_filter") val teamMemberIdFilter: TeamMemberIdFilter,
                              @SerialName("service_variation_id") val serviceVariationId: String = "")