package com.flepper.therapeutic.data.models

import com.flepper.therapeutic.data.models.customer.ReferenceId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Filter(@SerialName("reference_id") val referenceId: ReferenceId)