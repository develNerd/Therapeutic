package com.flepper.therapeutic.android.data

import androidx.annotation.DrawableRes
import com.flepper.therapeutic.android.R

data class EutiGoal(
    @DrawableRes val icon:Int,
    val description:String,
    )

val eutiGoals = listOf(
    EutiGoal(R.drawable.ic_love_box,"I’m very conversational and always here to suggest yur next step in your healing process"),
    EutiGoal(R.drawable.ic_clock,"Easily schedule appointments to talk to some of the best therapists whilst still staying anonymous"),
    EutiGoal(R.drawable.ic_person_with_circle,"I’ll remind you of all our free therapy events happening all around the world, as we find peace together"),
    )