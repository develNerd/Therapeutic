package com.flepper.therapeutic.data.reposositoryimpl

import co.touchlab.kermit.Logger
import com.flepper.therapeutic.data.FEATURED_CONTENT
import com.flepper.therapeutic.data.TherapeuticDb
import com.flepper.therapeutic.data.WORD_WIDE_EVENTS
import com.flepper.therapeutic.data.models.FeaturedContent
import com.flepper.therapeutic.data.models.WorldWideEvent
import com.flepper.therapeutic.data.models.WorldWideEventDAO
import com.flepper.therapeutic.data.models.WorldWideEventDTO
import com.flepper.therapeutic.data.repositories.EventsRepository
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventsRepositoryImp(private val fireStore: FirebaseFirestore, therapeuticDb: TherapeuticDb):EventsRepository {

    private val db = therapeuticDb.invoke()

    override suspend fun getFeaturedContent(): FlowList<FeaturedContent> = fireStore.collection(FEATURED_CONTENT).getData()
    override suspend fun getWorldEvents(): FlowList<WorldWideEventDTO> = fireStore.collection(
        WORD_WIDE_EVENTS).getData()

    override suspend fun saveWorldWideEvent(worldWideEvent: WorldWideEvent) {
        Logger.e("Dao" + worldWideEvent.toString())
        db.write {
            copyToRealm(worldWideEvent.getDAO().apply {
                   hosts = worldWideEvent.getDAO().hosts
            },UpdatePolicy.ALL)
        }
    }

    override suspend fun getWorldEvent(eventId:String): Flow<WorldWideEvent> {
        return db.query<WorldWideEventDAO>("id == $0", eventId).first().find()!!.asFlow().map { it.obj!!.toWorldEvent() }
    }
}


inline fun <reified T : Any> CollectionReference.getData():Flow<List<T>>{
   return this.snapshots.map { snapshot -> snapshot.documents.map { it.data() } }
}

typealias FlowList<T> = Flow<List<T>>

