package com.flepper.therapeutic.data.usecases

import com.flepper.therapeutic.data.models.WorldWideEvent
import com.flepper.therapeutic.data.repositories.EventsRepository
import com.flepper.therapeutic.data.reposositoryimpl.FlowList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWorldEventsUseCase(coroutineScope: CoroutineScope, private val eventsRepository: EventsRepository) :
    BaseUseCaseDispatcher<Unit, FlowList<WorldWideEvent>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: Unit,
        coroutineScope: CoroutineScope
    ) = eventsRepository.getWorldEvents().map { flow -> flow.map { it.toEventDataObject() } }

}

class SaveEventsLocalUseCase(coroutineScope: CoroutineScope, private val eventsRepository: EventsRepository) :
    BaseUseCaseDispatcher<WorldWideEvent, Unit>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: WorldWideEvent,
        coroutineScope: CoroutineScope
    ) = eventsRepository.saveWorldWideEvent(request)

}

class GetWorldEventLocalUseCase(coroutineScope: CoroutineScope, private val eventsRepository: EventsRepository) :
    BaseUseCaseDispatcher<String, Flow<WorldWideEvent>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: String,
        coroutineScope: CoroutineScope
    ) = eventsRepository.getWorldEvent(request)

}