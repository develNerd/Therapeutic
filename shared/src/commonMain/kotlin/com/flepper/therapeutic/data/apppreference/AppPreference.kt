package com.flepper.therapeutic.data.apppreference

import com.flepper.therapeutic.data.CurrentUser
import com.flepper.therapeutic.data.AnonUser
import com.flepper.therapeutic.data.apppreference.KMMPreference.getBool
import com.flepper.therapeutic.data.apppreference.KMMPreference.getInt
import com.flepper.therapeutic.data.apppreference.KMMPreference.getString
import com.flepper.therapeutic.data.apppreference.KMMPreference.putBool
import com.flepper.therapeutic.data.apppreference.KMMPreference.putInt
import com.flepper.therapeutic.data.apppreference.KMMPreference.putString
import com.flepper.therapeutic.di.KMMContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AppPreference(private val context: KMMContext) {

    private val CURRENT_USER = "CURRENT_USER"
    private val SIGN_IN_USER = "SIGN_IN_USER"
    private val IS_DASHBOARD = "IS_DASHBOARD"
    private val ACCESS_TOKEN = "ACCESS_TOKEN"
    private val REFRESH_TOKEN = "REFRESH_TOKEN"
    private val IS_TEAM_MEMBERS_SAVED = "isTeamMembersSaved"


    var anonUser: AnonUser?
        get() {
            return context.getString(CURRENT_USER)?.let { Json.decodeFromString<AnonUser>(it) }
        }
        set(value) {
            context.putString(CURRENT_USER, Json.encodeToString(value))
        }

    var signInUser: CurrentUser?
        get() {
            return context.getString(SIGN_IN_USER)?.let { Json.decodeFromString<CurrentUser>(it) }
        }
        set(value) {
            context.putString(SIGN_IN_USER, Json.encodeToString(value))
        }

    //TODO(In Production App Access Tokens will be gotten from a secure server)
    //TODO(Please revoke token after hackerton)
    //TODO(Token Should be gotten from the Web and refreshed every week)
    var accessToken: String
        get() = context.getString(ACCESS_TOKEN) ?: ""
        set(value) = context.putString(ACCESS_TOKEN, value)

    //TODO(In Production App Access Tokens will be gotten from a secure server)
    //TODO(Please revoke token after hackerton)
    //TODO(Token Should be gotten from the Web and refreshed every week)
    var refreshToken: String
        get() = context.getString(REFRESH_TOKEN) ?: ""
        set(value) = context.putString(REFRESH_TOKEN, value)

    var isBeenToDashboard: Boolean
        get() = context.getBool(IS_DASHBOARD, false)
        set(value) = context.putBool(IS_DASHBOARD, value)

    var isTeamMembersSaved: Boolean
        get() = context.getBool(IS_TEAM_MEMBERS_SAVED, false)
        set(value) = context.putBool(IS_TEAM_MEMBERS_SAVED, value)


    fun put(key: String, value: Int) {
        context.putInt(key, value)
    }

    fun put(key: String, value: String) {
        context.putString(key, value)
    }

    fun put(key: String, value: Boolean) {
        context.putBool(key, value)
    }

    fun getInt(key: String, default: Int): Int = context.getInt(key, default)


    fun getString(key: String): String? = context.getString(key)


    fun getBool(key: String, default: Boolean): Boolean =
        context.getBool(key, default)

}

