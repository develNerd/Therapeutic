package com.flepper.therapeutic.data.usecases

import com.flepper.therapeutic.data.SignInRequest
import com.flepper.therapeutic.data.CurrentUser
import com.flepper.therapeutic.data.repositories.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class SignInUseCase(coroutineScope: CoroutineScope, private val  authRepository: AuthRepository) :
    BaseUseCaseDispatcher<SignInRequest, Flow<CurrentUser>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: SignInRequest,
        coroutineScope: CoroutineScope
    ): Flow<CurrentUser> = authRepository.signInWithEmailAndPassword(request)

}

class SignInWithGoogleUseCase(coroutineScope: CoroutineScope, private val  authRepository: AuthRepository) :
    BaseUseCaseDispatcher<String, Flow<CurrentUser>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: String,
        coroutineScope: CoroutineScope
    ): Flow<CurrentUser> = authRepository.signInWithGoogle(request)

}