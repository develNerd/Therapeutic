package com.flepper.therapeutic.data.repositories

import com.flepper.therapeutic.data.SignInRequest
import com.flepper.therapeutic.data.CurrentUser
import com.flepper.therapeutic.data.SignUpRequest
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(
        signInRequest: SignInRequest
    ): Flow<CurrentUser>

    suspend fun signUp(
        signUpRequest: SignUpRequest
    ): Flow<CurrentUser>

    suspend fun signInWithGoogle(
        idToken:String
    ): Flow<CurrentUser>
}