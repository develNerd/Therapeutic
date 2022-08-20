package com.flepper.therapeutic.data.models

import kotlinx.serialization.Serializable

@Serializable
data class SquareSearchQuery(val filter: Filter)