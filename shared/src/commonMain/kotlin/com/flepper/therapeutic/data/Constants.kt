package com.flepper.therapeutic.data

import com.flepper.therapeutic.data.network.ApiResult
import kotlinx.coroutines.flow.Flow


typealias  FlowResult<T> = Flow<ApiResult<T>>
const val appBaseUrl = "connect.squareupsandbox.com/v2"

//collection paths
const val FEATURED_CONTENT = "FeaturedContent"
const val WORD_WIDE_EVENTS = "WorldWideEvents"
const val COMPANY_NAME = "Therapeutic"
const val DEFAULT_NOTE = "Customer Created using Euti"
const val COMPANY_PHONE_NUMBER = ""
const val SQUARE_API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
const val DEFAULT_SESSION_DURATION = 45
const val DEFAULT_CUSTOMER_NOTE = "Appointment for Therapy Session"
const val DEFAULT_SERVICE_VERSION =1661086675016
const val DEFAULT_LOCATION_TYPE = "BUSINESS_LOCATION"
const val UTC_TIMEZONE_OFFSET = 5

// -> End Collection paths

