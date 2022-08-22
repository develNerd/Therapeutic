package com.flepper.therapeutic.android.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import com.flepper.therapeutic.android.R


const val sliderChangeTime:Long = 10000
const val progressBarAnimationOffset: Int =  1/10000
const val EQUALISER_DRAWABLE_INT = R.drawable.ic_equaliser

fun Context.launchTwitter(){
    try {
        // Check if the Twitter app is installed on the phone.
        packageManager.getPackageInfo("com.twitter.android", 0)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity")
        // Don't forget to put the "L" at the end of the id.
        intent.putExtra("user_id", 1469351024800710658L)
        this.startActivity(intent)
    } catch (e: PackageManager.NameNotFoundException) {
        // If Twitter app is not installed, start browser.
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/isaacAIKE")
            )
        )
    }
}
