package com.flepper.therapeutic.data

import com.flepper.therapeutic.data.network.ApiResult
import kotlinx.coroutines.flow.Flow


typealias  FlowResult<T> = Flow<ApiResult<T>>
const val appBaseUrl = "connect.squareupsandbox.com/v2"

//collection paths
const val FEATURED_CONTENT = "FeaturedContent"
const val WORD_WIDE_EVENTS = "WorldWideEvents"


// -> End Collection paths

