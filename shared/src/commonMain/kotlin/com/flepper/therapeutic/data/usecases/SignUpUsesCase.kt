package com.flepper.therapeutic.data.usecases

import com.flepper.therapeutic.data.CurrentUser
import com.flepper.therapeutic.data.SignUpRequest
import com.flepper.therapeutic.data.repositories.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class SignUpUsesCase(coroutineScope: CoroutineScope, private val  authRepository: AuthRepository) :
    BaseUseCaseDispatcher<SignUpRequest, Flow<CurrentUser>>(coroutineScope) {
    override suspend fun dispatchInBackground(
        request: SignUpRequest,
        coroutineScope: CoroutineScope
    ): Flow<CurrentUser> = authRepository.signUp(request)

}