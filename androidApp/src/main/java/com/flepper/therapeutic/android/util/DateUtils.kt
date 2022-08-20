package com.flepper.therapeutic.android.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.text.SimpleDateFormat
import java.util.*

fun LocalDateTime.parseToMonthDayString(): String {
    val dateFormat = SimpleDateFormat(
        "EEE, MMM d",
        Locale.getDefault()
    )
    val date = Date(this.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds())
    return dateFormat.format(date)
}

fun LocalDateTime.parseToHourMinuteString(): String {
    val dateFormat = SimpleDateFormat(
        "hh:mm aa",
        Locale.getDefault()
    )
    val date = Date(this.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds())
    return dateFormat.format(date)
}