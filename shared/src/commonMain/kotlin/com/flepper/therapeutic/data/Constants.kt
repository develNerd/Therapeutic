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
const val SQUARE_API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.S'Z'"


// -> End Collection paths

