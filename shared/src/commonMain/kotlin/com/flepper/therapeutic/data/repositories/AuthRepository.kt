package com.flepper.therapeutic.data.repositories

import com.flepper.therapeutic.data.SignInRequest
import com.flepper.therapeutic.data.SignInUser
import com.flepper.therapeutic.data.SignUpRequest
import dev.gitlive.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(
        signInRequest: SignInRequest
    ): Flow<SignInUser>

    suspend fun signUp(
        signUpRequest: SignUpRequest
    ): Flow<SignInUser>

    suspend fun signInWithGoogle(
        idToken:String
    ): Flow<SignInUser>
}