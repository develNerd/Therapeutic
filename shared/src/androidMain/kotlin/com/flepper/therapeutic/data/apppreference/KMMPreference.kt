package com.flepper.therapeutic.data.apppreference

import com.flepper.therapeutic.di.Context
import com.flepper.therapeutic.di.KMMContext

actual object KMMPreference {

    const val SP_NAME = "kmm_app"

    private fun KMMContext.getSp() = this.application.getSharedPreferences(SP_NAME, 0)

    private fun KMMContext.getSpEditor() =  getSp().edit()

    actual fun KMMContext.putInt(key: String, value: Int) {
        getSpEditor().putInt(key, value).apply()
    }

    actual fun KMMContext.getInt(key: String, default: Int): Int {
        return  getSp().getInt(key, default )
    }

    actual fun KMMContext.putString(key: String, value: String) {
        getSpEditor().putString(key, value).apply()
    }

    actual fun KMMContext.getString(key: String): String? {
        return  getSp().getString(key, null)
    }

    actual fun KMMContext.putBool(key: String, value: Boolean) {
        getSpEditor().putBoolean(key, value).apply()
    }

    actual fun KMMContext.getBool(key: String, default: Boolean): Boolean {
        return getSp().getBoolean(key, default)
    }

}