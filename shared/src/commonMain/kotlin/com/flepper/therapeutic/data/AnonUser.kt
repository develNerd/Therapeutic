package com.flepper.therapeutic.data

import kotlinx.serialization.Serializable

@Serializable
data class AnonUser(val userName:String)

@Serializable
data class CurrentUser(var id:String, val userName:String, val email:String, var squareCustomerID:String = "")


@Serializable
data class SignInRequest(val email:String,var password:String)

@Serializable
data class SignUpRequest(val userName: String,val email:String,var password:String)
