package com.flepper.therapeutic.android.util

import android.content.Context
import android.util.Log
import com.flepper.therapeutic.android.R
import java.net.URLEncoder

fun resolveVideoUrl(context:Context,name:String):String{
    val url = context.getString(R.string.video_url,"%2F", URLEncoder.encode(name, "utf-8"))
    Log.e("UrlResolved - ",url)
    return url
}

fun resolveImageUrl(context:Context,name:String):String{
    val url = context.getString(R.string.image_url,"%2F", URLEncoder.encode(name, "utf-8"))
    Log.e("ImageUrlResolved - ",url)
    return url
}

fun resolveAvatarUrl(context:Context,name:String):String{
    val url = context.getString(R.string.avatar_url,"%2F", URLEncoder.encode(name, "utf-8"))
    Log.e("ImageUrlResolved - ",url)
    return url
}