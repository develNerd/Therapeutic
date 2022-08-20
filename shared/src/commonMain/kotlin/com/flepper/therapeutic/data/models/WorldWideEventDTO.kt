package com.flepper.therapeutic.data.models

import com.flepper.therapeutic.util.getLocaleDateTime
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WorldWideEventDTO(
    var id: String,
    @SerialName("start_time") val startTime: String = "",
    @SerialName("hash_tag") val hashTag: String = "",
    @SerialName("is_ongoing") val isOngoing: Boolean = false,
    val hosts: Map<String, String>?,
    @SerialName("date_created") val dateCreated: String = "",
    val description: String = "",
    val title: String = "",
    val category: String = ""
) {
    fun toEventDataObject() = WorldWideEvent(
        id = id,
        startTime = startTime.getLocaleDateTime(),
        hashTag = hashTag,
        isOngoing = isOngoing,
        hosts = hosts?.map { Host(it.key, it.value) },
        dateCreated = dateCreated.getLocaleDateTime(),
        description = description,
        title = title,
        category = category
    )

}

data class WorldWideEvent(
    val id: String,
    val startTime: LocalDateTime?,
    val hashTag: String = "",
    val isOngoing: Boolean = false,
    val hosts: List<Host>?,
    val dateCreated: LocalDateTime?,
    val description: String = "",
    val title: String = "",
    val category: String = "",
    var isAttending: Boolean = false,
    var backGroundColor: Any? = null
) {
    fun getDAO() = WorldWideEventDAO().also { dao ->
        dao.id = id
        dao.startTime = startTime.toString()
        dao.hashTag = hashTag
        dao.isOngoing = isOngoing
        dao.hosts = hosts?.map { it.hostDao() }
        dao.dateCreated = dateCreated.toString()
        dao.description = description
        dao.title = title
        dao.category = category
        dao.isAttending = isAttending
        dao.backGroundColor = ""
    }

}

class WorldWideEventDAO : RealmObject {
    @PrimaryKey
    var id: String = ""
    var startTime: String = ""
    var hashTag: String = ""
    var isOngoing: Boolean = false
    var hosts: List<HostDao>? = null
    var dateCreated: String = ""
    var description: String = ""
    var title: String = ""
    var category: String = ""
    var isAttending: Boolean = false
    var backGroundColor: String? = null

    fun toWorldEvent():WorldWideEvent{
        return WorldWideEvent(id,startTime.toLocalDateTime(),hashTag,isOngoing,hosts?.map { it.toHosts() },null,description, title, category, isAttending, backGroundColor)
    }
}


