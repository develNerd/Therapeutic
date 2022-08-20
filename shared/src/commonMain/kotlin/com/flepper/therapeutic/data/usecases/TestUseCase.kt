package com.flepper.therapeutic.data.usecases

import com.flepper.therapeutic.data.FlowResult
import com.flepper.therapeutic.data.models.Auction
import com.flepper.therapeutic.data.repositories.TestRepository
import kotlinx.coroutines.CoroutineScope

class TestUseCase(coroutineScope: CoroutineScope, private val testRepository: TestRepository) :
    BaseUseCaseDispatcher<Unit, FlowResult<List<Auction>>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: Unit,
        coroutineScope: CoroutineScope
    ) = testRepository.getTestDocsString()

}

class CodeUseCase(coroutineScope: CoroutineScope, private val testRepository: TestRepository) :
    BaseUseCaseDispatcher<Unit, FlowResult<String>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: Unit,
        coroutineScope: CoroutineScope
    ) = testRepository.getCode()

}