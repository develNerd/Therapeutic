package com.flepper.therapeutic.data

import com.flepper.therapeutic.data.models.HostDao
import com.flepper.therapeutic.data.models.TeamMembersItem
import com.flepper.therapeutic.data.models.TeamMembersItemDao
import com.flepper.therapeutic.data.models.WorldWideEventDAO
import com.flepper.therapeutic.data.models.appointments.booking.AppointmentSegmentItemDao
import com.flepper.therapeutic.data.models.appointments.booking.BookAppointmentResponseDao
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class TherapeuticDb {
    private val configuration =RealmConfiguration.Builder(schema = setOf(WorldWideEventDAO::class,HostDao::class,TeamMembersItemDao::class,BookAppointmentResponseDao::class,AppointmentSegmentItemDao::class)).schemaVersion(4).build()

    operator fun invoke() = Realm.open(configuration.apply { deleteRealmIfMigrationNeeded })

}