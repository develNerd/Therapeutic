package com.flepper.therapeutic.data.models.customer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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

@Serializable
data class GetCustomersResponse(@SerialName("customers") val customers:List<CustomerResponse>)

@Serializable
data class CustomerWrapperResponse(val customer:CustomerResponse)

@Serializable
data class CustomerResponse(
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
    @SerialName("id")
    val customer_id: String = "",
    )

