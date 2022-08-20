package com.flepper.therapeutic.android.di

import android.app.Application
import android.content.Context
import com.flepper.therapeutic.android.presentation.MainActivityViewModel
import com.flepper.therapeutic.data.apppreference.AppPreference
import com.flepper.therapeutic.di.KMMContext
import com.flepper.therapeutic.di.netWorkingModule
import com.flepper.therapeutic.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val presentationModule = module {
    single { ApplicationContext(androidContext()) }
    single { MainActivityViewModel() }
    single { KMMContext(androidContext() as Application) }
    single { AppPreference(get()) }
}

class ApplicationContext(private val context: Context){
    operator fun invoke():Context{
        return context
    }
}
