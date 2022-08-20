package com.flepper.therapeutic.data.usecases

import com.flepper.therapeutic.data.models.FeaturedContent
import com.flepper.therapeutic.data.repositories.EventsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class GetFeaturedContentUseCase(coroutineScope: CoroutineScope, private val firebaseRepository: EventsRepository) :
    BaseUseCaseDispatcher<Unit, Flow<List<FeaturedContent>>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: Unit,
        coroutineScope: CoroutineScope
    ) = firebaseRepository.getFeaturedContent()

}