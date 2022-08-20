package com.flepper.therapeutic.data.models

import io.realm.kotlin.types.RealmObject


data class Host(val name:String = "",val avatar:String = "") {
    fun hostDao() = HostDao().also { dao ->
        dao.name = name
        dao.avatar = avatar
    }
}

class HostDao:RealmObject {
    var name:String = ""
    var avatar:String = ""
    fun toHosts() = Host(name, avatar)
}