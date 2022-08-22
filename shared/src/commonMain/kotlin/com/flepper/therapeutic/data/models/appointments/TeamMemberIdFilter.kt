package com.flepper.therapeutic.data.models.appointments

import kotlinx.serialization.Serializable


@Serializable
data class TeamMemberIdFilter(val any: List<String>?)