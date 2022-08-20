package com.flepper.therapeutic.util

import co.touchlab.kermit.Logger
import io.ktor.http.parsing.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

fun String.getLocaleDateTime(): LocalDateTime? {
    return try {
        
        var dateString = this
        if (this.contains("Z")){
           dateString = dateString.replace('Z',' ')
        }
        LocalDateTime.parse(dateString.trim())
    } catch (e: Exception) {
        Logger.e("StringError"){
            this
        }
        Logger.e(e.message?: "",e)
        null
    }
}

