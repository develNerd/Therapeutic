package com.flepper.therapeutic.android.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.flepper.therapeutic.android.BuildConfig
import com.flepper.therapeutic.android.presentation.core.BaseViewModel
import com.flepper.therapeutic.android.presentation.home.euti.EutiViewModel
import com.flepper.therapeutic.data.apppreference.AppPreference
import com.flepper.therapeutic.data.AnonUser
import com.flepper.therapeutic.data.CurrentUser
import com.flepper.therapeutic.data.models.Auction
import com.flepper.therapeutic.data.models.FeaturedContent
import com.flepper.therapeutic.data.models.Filter
import com.flepper.therapeutic.data.models.TeamMembersItem
import com.flepper.therapeutic.data.models.customer.ReferenceId
import com.flepper.therapeutic.data.usecasefactories.AppointmentsUseCaseFactory
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.inject

class MainActivityViewModel : BaseViewModel() {

    val appPreferences: AppPreference by inject()

    private val appointmentsUseCaseFactory: AppointmentsUseCaseFactory by inject()

    private val _auctionsState = MutableStateFlow(OnResultObtained<List<Auction>>(null,false))
    private val auctionsState: MutableStateFlow<OnResultObtained<List<Auction>>>
        get() = _auctionsState

    private val _featuredContent = MutableStateFlow(OnResultObtained<List<FeaturedContent>>(null,false))
    private val featuredContent: MutableStateFlow<OnResultObtained<List<FeaturedContent>>>
        get() = _featuredContent


    fun saveUser(userName: String) {
        appPreferences.accessToken = BuildConfig.SQUARE_ACCESS_TOKEN
        appPreferences.refreshToken = BuildConfig.SQUARE_REFRESH_TOKEN
        appPreferences.anonUser = AnonUser(userName)
    }

/*
    fun getCode(){
        executeApiCallUseCase(
            viewModelScope = viewModelScope,
            useCase = testUseCase,
            callback = { result ->
                Log.e("Result",result.toString())
            },
            onError = {
                _auctionsState.value = OnResultObtained(null,true)
                Log.e("Result",it.message.toString())
            })
    }
*/


   /* fun tryTest() {
        executeUseCase(
            viewModelScope = viewModelScope,
            useCase = testUseCase,
            callback = { result ->
                Log.e("Result",result.toString())
                _auctionsState.value = OnResultObtained(result,true)
            },
            onError = {
                _auctionsState.value = OnResultObtained(null,true)
                Log.e("Result",it.message.toString())
            })
    }*/
    /** Get Team members if customer does not exist and return if exists */
    fun getTeamMembers() {
        if (!appPreferences.isTeamMembersSaved){
            executeApiCallUseCase(
                viewModelScope = viewModelScope,
                useCase = appointmentsUseCaseFactory.getTeamMembersUseCase,
                callback = { result ->
                    saveTeamMembersLocal(result)
                },
                onError = {
                    /** Create if customer does not exist*/
                    Log.e("Result", it.message.toString())
                })
        }
    }

    fun saveTeamMembersLocal(request:List<TeamMembersItem>) {
        executeLocalUseCase(
            viewModelScope = viewModelScope,
            inputValue = request,
            useCase = appointmentsUseCaseFactory.saveTeamMemberLocalUseCase,
            callback = { result ->
                appPreferences.isTeamMembersSaved = true
            },
            onError = {
                /** Create if customer does not exist*/

                Log.e("Result", it.message.toString())
            })
    }


}