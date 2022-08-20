package com.flepper.therapeutic.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeaturedContent(
    val description:String,
    val duration:String,
    @SerialName("video_link")
    val video:String,
    @SerialName("thumbnail")
    val thumbNail:String

)
