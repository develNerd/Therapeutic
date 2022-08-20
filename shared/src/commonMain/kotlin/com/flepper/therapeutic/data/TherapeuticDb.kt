package com.flepper.therapeutic.data

import com.flepper.therapeutic.data.models.HostDao
import com.flepper.therapeutic.data.models.WorldWideEventDAO
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class TherapeuticDb {
    private val configuration =RealmConfiguration.Builder(schema = setOf(WorldWideEventDAO::class,HostDao::class)).schemaVersion(2).build()

    operator fun invoke() = Realm.open(configuration.apply { deleteRealmIfMigrationNeeded })
}