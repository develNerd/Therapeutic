package com.flepper.therapeutic.data.models.customer

import com.flepper.therapeutic.data.models.SquareSearchQuery
import kotlinx.serialization.Serializable

@Serializable
data class SearchCustomer(val query: SquareSearchQuery)