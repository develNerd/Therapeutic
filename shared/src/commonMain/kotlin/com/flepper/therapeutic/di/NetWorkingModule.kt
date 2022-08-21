package com.flepper.therapeutic.di

import com.flepper.therapeutic.data.TherapeuticDb
import com.flepper.therapeutic.data.apppreference.AppPreference
import com.flepper.therapeutic.data.network.Api
import com.flepper.therapeutic.data.network.KtorHttpClient
import com.flepper.therapeutic.data.network.SquareHttpClient
import com.flepper.therapeutic.data.repositories.*
import com.flepper.therapeutic.data.reposositoryimpl.AppointmentsRepositoryImpl
import com.flepper.therapeutic.data.reposositoryimpl.AuthRepositoryImpl
import com.flepper.therapeutic.data.reposositoryimpl.EventsRepositoryImp
import com.flepper.therapeutic.data.usecasefactories.AppointmentsUseCaseFactory
import com.flepper.therapeutic.data.usecasefactories.AuthUseCaseFactory
import com.flepper.therapeutic.data.usecasefactories.HomeUseCaseFactory
import com.flepper.therapeutic.data.usecases.CodeUseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.MainScope
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val netWorkingModule = module {
    single { MainScope() }
    single { Firebase.firestore.apply { setSettings(persistenceEnabled = true) } }
    singleOf(::AppPreference)
    singleOf(::KtorHttpClient)
    single { SquareHttpClient(get()) }
    single { Api(get()) }
    singleOf(::TherapeuticDb)
}

val repositoryModule = module {
    single<TestRepository> { TestRepositoryImpl(get()) }
    single<EventsRepository> { EventsRepositoryImp(get(),get()) }
    single<AuthRepository> { AuthRepositoryImpl() }
    single<AppointmentsRepository> { AppointmentsRepositoryImpl(get(),get()) }
}

val useCaseFactoryModule = module {
    single { CodeUseCase(get(),get()) }
    single { HomeUseCaseFactory(get(),get()) }
    single { AuthUseCaseFactory(get(),get()) }
    single { AppointmentsUseCaseFactory(get(),get()) }
}

