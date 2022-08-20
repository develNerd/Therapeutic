package com.flepper.therapeutic.android.presentation.home

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.flepper.therapeutic.android.presentation.core.BaseViewModel
import com.flepper.therapeutic.android.presentation.theme.eventColors
import com.flepper.therapeutic.data.apppreference.AppPreference
import com.flepper.therapeutic.data.models.FeaturedContent
import com.flepper.therapeutic.data.models.WorldWideEvent
import com.flepper.therapeutic.data.usecasefactories.HomeUseCaseFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.inject

class HomeViewModel : BaseViewModel() {

    val appPreferences: AppPreference by inject()

    private val homeUseCaseFactory: HomeUseCaseFactory by inject()


    private val _currentFeaturedContent = MutableStateFlow(emptyList<FeaturedContent>())
    val currentFeaturedContent: StateFlow<List<FeaturedContent>>
        get() = _currentFeaturedContent

    fun setCurrentFeaturedContent(featuredContent: FeaturedContent){
        _currentFeaturedContent.value = listOf(featuredContent)
    }

    private val _featuredContent =
        MutableStateFlow(OnResultObtained<List<FeaturedContent>>(null, false))
    val featuredContent: StateFlow<OnResultObtained<List<FeaturedContent>>>
        get() = _featuredContent

    private val _events = MutableStateFlow(OnResultObtained<List<WorldWideEvent>>(null, false))
    val events: StateFlow<OnResultObtained<List<WorldWideEvent>>>
        get() = _events

    private val _selectedEvent = MutableStateFlow(emptyList<WorldWideEvent>())
    val selectedEvent: StateFlow<List<WorldWideEvent>>
        get() = _selectedEvent



    private val _changeMade = MutableStateFlow(false)
    val changeMade: MutableStateFlow<Boolean>
        get() = _changeMade

    fun refreshEventSelection(){
        _changeMade.value = !_changeMade.value
    }

    fun setSelectedEvent(event: WorldWideEvent?){
        _selectedEvent.value = if (event!= null) listOf(event) else emptyList()
    }

    fun getEvents() {
        executeFirebaseUseCase(
            viewModelScope = viewModelScope,
            useCase = homeUseCaseFactory.getWorldEventsUseCase,
            state = _events,
            callback = { result ->
                val backgroundColors = mutableListOf<Color>()
                var currentColorIndex = 0
                val eventResult = result.mapIndexed { index, worldWideEvent ->
                    if (backgroundColors.isEmpty()) {
                        backgroundColors.addAll(eventColors)
                    }
                    val itemColor = backgroundColors[currentColorIndex]
                    if (backgroundColors.contains(itemColor)){
                        backgroundColors.remove(itemColor)
                    }
                    worldWideEvent.backGroundColor = itemColor
                    if (currentColorIndex < backgroundColors.size - 1) currentColorIndex++ else currentColorIndex = 0
                    worldWideEvent
                }
                Log.e("Result", eventResult.toString())
                _events.value = OnResultObtained(eventResult, true)
            }, onError = {
                //assign to error variable
            })
    }


    fun saveEvent(worldWideEvent: WorldWideEvent) {
        Log.e("Saving-world", worldWideEvent.toString())

        executeLocalUseCase(
            viewModelScope = viewModelScope,
            useCase = homeUseCaseFactory.saveEventsLocalUseCase,
            inputValue = worldWideEvent,
            callback = { result ->
                Log.e("Result-Success", result.toString())

            }, onError = {
                //assign to error variable
                it.printStackTrace()
                Log.e("Error", it.toString())
            })
    }

    private val _currentLocalEvent = MutableStateFlow(emptyList<WorldWideEvent>())
    val currentLocalEvent: MutableStateFlow<List<WorldWideEvent>>
        get() = _currentLocalEvent


    fun getEvent(eventId:String,worldWideEvent: WorldWideEvent){
        executeLocalFlowUseCase(
            viewModelScope = viewModelScope,
            useCase = homeUseCaseFactory.getWorldEventLocalUseCase,
            inputValue = eventId,
            callback = { result ->
                _currentLocalEvent.value = listOf(result)
                worldWideEvent.apply { isAttending = result.isAttending }
                setSelectedEvent(worldWideEvent)

                Log.e("Result-Success", result.toString())
            }, onError = {
                saveEvent(worldWideEvent)
                setSelectedEvent(worldWideEvent)
                //assign to error variable
                Log.e("Error", it.toString())
            })
    }

    fun getFeaturedContent() {
        executeFirebaseUseCase(
            viewModelScope = viewModelScope,
            useCase = homeUseCaseFactory.getFeaturedContentUseCase,
            state = _featuredContent,
            callback = { result ->
                Log.e("Result", result.toString())
                _featuredContent.value = OnResultObtained(result, true)
            }, onError = {
                //assign to error variable
            })
    }
}