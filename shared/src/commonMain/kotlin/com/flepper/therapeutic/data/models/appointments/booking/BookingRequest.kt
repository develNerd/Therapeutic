package com.flepper.therapeutic.data.models.appointments.booking

import kotlinx.serialization.Serializable

@Serializable
data class BookingRequest(val booking: Booking)