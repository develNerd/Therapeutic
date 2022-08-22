package com.flepper.therapeutic.data.reposositoryimpl

import com.flepper.therapeutic.data.SignInRequest
import com.flepper.therapeutic.data.CurrentUser
import com.flepper.therapeutic.data.SignUpRequest
import com.flepper.therapeutic.data.repositories.AuthRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl : AuthRepository {
    override suspend fun signInWithEmailAndPassword(
        signInRequest: SignInRequest
    ): Flow<CurrentUser> = flow {
        signInRequest.apply {
            val user = Firebase.auth.signInWithEmailAndPassword(email, password).user
            emit(CurrentUser(user?.uid ?: "",user?.email ?: "",user?.email ?: ""))
        }
    }

    override suspend fun signUp(
        signUpRequest: SignUpRequest
    ): Flow<CurrentUser> = flow {
        signUpRequest.apply {
            val user = Firebase.auth.createUserWithEmailAndPassword(email, password).user
            user?.updateProfile(displayName = userName)
            emit(CurrentUser(user?.uid ?: "",user?.email ?: "",user?.email ?: ""))
        }
    }

    override suspend fun signInWithGoogle(idToken:String)  = flow {
        val credential = GoogleAuthProvider.credential(idToken = idToken,null)
        val user = Firebase.auth.signInWithCredential(credential).user
        emit(CurrentUser(user?.uid ?: "",user?.email ?: "",user?.email ?: ""))
    }


}