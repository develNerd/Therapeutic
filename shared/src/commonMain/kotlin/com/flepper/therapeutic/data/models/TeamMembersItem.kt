package com.flepper.therapeutic.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberData(@SerialName("team_members") val teamMembers:List<TeamMembersItem>)

@Serializable
data class TeamMembersItem(
    @SerialName("email_address") val emailAddress: String = "",
    @SerialName("reference_id") val referenceId: String = "",
    @SerialName("updated_at") val updatedAt: String = "",
    @SerialName("is_owner") val isOwner: Boolean = false,
    @SerialName("created_at") val createdAt: String = "",
    @SerialName("assigned_locations") val assignedLocations: AssignedLocations?,
    @SerialName("phone_number") val phoneNumber: String = "",
    @SerialName("id") val id: String = "",
    @SerialName("given_name") val givenName: String = "",
    @SerialName("family_name") val familyName: String = "",
    @SerialName("status") val status: String = ""
) {
    fun getDao() = TeamMembersItemDao().also { dao ->
        dao.id = id
        dao.givenName = givenName
        dao.referenceId = referenceId
        dao.phoneNumber = phoneNumber
        dao.emailAddress = emailAddress
        dao.familyName = familyName
        dao.status = status

    }
}


class TeamMembersItemDao : RealmObject {
    @PrimaryKey
    var id: String = ""
    var phoneNumber: String = ""
    var referenceId: String = ""
    var givenName: String = ""
    var emailAddress: String = ""
    var familyName: String = ""
    var status: String = ""

    fun toTeamMember() = TeamMembersItem(
        emailAddress,
        referenceId,
        "",
        false,
        "",
        null,
        phoneNumber,
        id,
        givenName,
        familyName,
        status
    )
}