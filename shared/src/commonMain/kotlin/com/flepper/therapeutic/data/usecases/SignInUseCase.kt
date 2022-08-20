package com.flepper.therapeutic.data.usecases

import com.flepper.therapeutic.data.SignInRequest
import com.flepper.therapeutic.data.SignInUser
import com.flepper.therapeutic.data.models.FeaturedContent
import com.flepper.therapeutic.data.repositories.AuthRepository
import com.flepper.therapeutic.data.repositories.EventsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class SignInUseCase(coroutineScope: CoroutineScope, private val  authRepository: AuthRepository) :
    BaseUseCaseDispatcher<SignInRequest, Flow<SignInUser>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: SignInRequest,
        coroutineScope: CoroutineScope
    ): Flow<SignInUser> = authRepository.signInWithEmailAndPassword(request)

}

class SignInWithGoogleUseCase(coroutineScope: CoroutineScope, private val  authRepository: AuthRepository) :
    BaseUseCaseDispatcher<String, Flow<SignInUser>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: String,
        coroutineScope: CoroutineScope
    ): Flow<SignInUser> = authRepository.signInWithGoogle(request)

}