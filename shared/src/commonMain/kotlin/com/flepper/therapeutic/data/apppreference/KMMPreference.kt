package com.flepper.therapeutic.data.apppreference

import com.flepper.therapeutic.di.KMMContext

expect object KMMPreference {
    fun KMMContext. putInt(key: String, value: Int)

    fun KMMContext.getInt(key: String, default: Int): Int

    fun KMMContext.putString(key: String, value: String)

    fun KMMContext.getString(key: String) : String?

    fun KMMContext.putBool(key: String, value: Boolean)

    fun KMMContext.getBool(key: String, default: Boolean): Boolean
}