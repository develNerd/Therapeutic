package com.flepper.therapeutic.data.usecasefactories

import com.flepper.therapeutic.data.repositories.EventsRepository
import com.flepper.therapeutic.data.usecases.GetFeaturedContentUseCase
import com.flepper.therapeutic.data.usecases.GetWorldEventLocalUseCase
import com.flepper.therapeutic.data.usecases.GetWorldEventsUseCase
import com.flepper.therapeutic.data.usecases.SaveEventsLocalUseCase
import kotlinx.coroutines.CoroutineScope

class HomeUseCaseFactory(coroutineScope: CoroutineScope, eventsRepository: EventsRepository) {
    val getFeaturedContentUseCase = GetFeaturedContentUseCase(coroutineScope,eventsRepository)
    val getWorldEventsUseCase = GetWorldEventsUseCase(coroutineScope,eventsRepository)
    val saveEventsLocalUseCase = SaveEventsLocalUseCase(coroutineScope,eventsRepository)
    val getWorldEventLocalUseCase = GetWorldEventLocalUseCase(coroutineScope,eventsRepository)

}