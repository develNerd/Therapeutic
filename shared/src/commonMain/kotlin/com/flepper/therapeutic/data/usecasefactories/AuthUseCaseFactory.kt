package com.flepper.therapeutic.data.usecasefactories

import com.flepper.therapeutic.data.repositories.AuthRepository
import com.flepper.therapeutic.data.repositories.EventsRepository
import com.flepper.therapeutic.data.usecases.SignInUseCase
import com.flepper.therapeutic.data.usecases.SignInWithGoogleUseCase
import com.flepper.therapeutic.data.usecases.SignUpUsesCase
import kotlinx.coroutines.CoroutineScope

class AuthUseCaseFactory(coroutineScope: CoroutineScope, authRepository: AuthRepository) {
    val signInUseCase = SignInUseCase(coroutineScope,authRepository)
    val signUpUseCase = SignUpUsesCase(coroutineScope, authRepository)
    val signInWithGoogleUseCase = SignInWithGoogleUseCase(coroutineScope,authRepository)
}