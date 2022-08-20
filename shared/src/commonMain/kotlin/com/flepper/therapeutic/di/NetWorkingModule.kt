package com.flepper.therapeutic.di

import com.flepper.therapeutic.data.TherapeuticDb
import com.flepper.therapeutic.data.apppreference.AppPreference
import com.flepper.therapeutic.data.network.Api
import com.flepper.therapeutic.data.network.KtorHttpClient
import com.flepper.therapeutic.data.repositories.AuthRepository
import com.flepper.therapeutic.data.repositories.EventsRepository
import com.flepper.therapeutic.data.repositories.TestRepository
import com.flepper.therapeutic.data.repositories.TestRepositoryImpl
import com.flepper.therapeutic.data.reposositoryimpl.AuthRepositoryImpl
import com.flepper.therapeutic.data.reposositoryimpl.EventsRepositoryImp
import com.flepper.therapeutic.data.usecasefactories.AuthUseCaseFactory
import com.flepper.therapeutic.data.usecasefactories.HomeUseCaseFactory
import com.flepper.therapeutic.data.usecases.CodeUseCase
import com.flepper.therapeutic.data.usecases.TestUseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.MainScope
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val netWorkingModule = module {
    single { MainScope() }
    single { Firebase.firestore.apply { setSettings(persistenceEnabled = false) } }
    singleOf(::AppPreference)
    singleOf(::KtorHttpClient)
    single { Api(get()) }
    singleOf(::TherapeuticDb)
}

val repositoryModule = module {
    single<TestRepository> { TestRepositoryImpl(get()) }
    single<EventsRepository> { EventsRepositoryImp(get(),get()) }
    single<AuthRepository> { AuthRepositoryImpl() }
}

val useCaseFactoryModule = module {
    single { TestUseCase(get(),get()) }
    single { CodeUseCase(get(),get()) }
    single { HomeUseCaseFactory(get(),get()) }
    single { AuthUseCaseFactory(get(),get()) }
}

