package com.flepper.therapeutic.data.models.customer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Customer(
                    @SerialName("email_address")
                    val emailAddress: String = "",
                    @SerialName("reference_id")
                    val referenceId: String = "",
                    @SerialName("company_name")
                    val companyName: String = "Therapeutic",
                    val note: String = "",
                    @SerialName("given_name")
                    val givenName: String = "",
                    @SerialName("family_name")
                    val familyName: String = "",
                    @SerialName("phone_number")
                    val phoneNumber: String = "",
)

