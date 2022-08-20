package com.flepper.therapeutic.data.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

abstract class BaseUseCaseDispatcher<Input,Output > constructor(private val coroutineContext: CoroutineScope) :BaseUseCase<Input,Output>  {
    override suspend fun execute(request: Input, callback: suspend (Output) -> Unit) {
        val result = withContext(coroutineContext.coroutineContext){
            dispatchInBackground(request,this)
        }
        callback(result)
    }

    abstract suspend fun dispatchInBackground(
        request: Input,
        coroutineScope: CoroutineScope
    ): Output
}

interface BaseUseCase<Input, Output> {
    suspend  fun execute(request: Input, callback: suspend (Output) -> Unit)
    fun onError(throwable: Throwable): Exception = Exception(throwable.message)
}

