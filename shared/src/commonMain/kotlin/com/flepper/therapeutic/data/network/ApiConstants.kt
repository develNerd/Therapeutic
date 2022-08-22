package com.flepper.therapeutic.data.network

const val CUSTOMERS = "customers"
const val CUSTOMERS_SEARCH = "customers/search"
const val TEAM_MEMBER_SEARCH = "team-members/search"
const val SEARCH_AVAILABILITY = "bookings/availability/search"
const val BOOK_APPOINTMENT = "bookings"
fun cancelAppointmentUrl(bookingID:String) = "bookings/${bookingID}/cancel"