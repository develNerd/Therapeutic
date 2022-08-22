package com.flepper.therapeutic.android.presentation.home

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.presentation.core.BaseViewModel
import com.flepper.therapeutic.android.presentation.home.euti.EutiMainSheetItem
import com.flepper.therapeutic.android.presentation.home.euti.SheetContentType
import com.flepper.therapeutic.android.presentation.theme.eventColors
import com.flepper.therapeutic.android.util.asSquareApiDateString
import com.flepper.therapeutic.android.util.toAppointmentEndCalendar
import com.flepper.therapeutic.android.util.toAppointmentStartCalendar
import com.flepper.therapeutic.data.apppreference.AppPreference
import com.flepper.therapeutic.data.models.FeaturedContent
import com.flepper.therapeutic.data.models.TeamMembersItem
import com.flepper.therapeutic.data.models.WorldWideEvent
import com.flepper.therapeutic.data.models.appointments.StartAtRange
import com.flepper.therapeutic.data.models.appointments.booking.BookAppointmentResponse
import com.flepper.therapeutic.data.usecasefactories.AppointmentsUseCaseFactory
import com.flepper.therapeutic.data.usecasefactories.HomeUseCaseFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.inject

class HomeViewModel : BaseViewModel() {

    val appPreferences: AppPreference by inject()

    private val _currentBottomSheetContentType = MutableStateFlow(BottomSheetContentType.EVENT)
    val currentBottomSheetContentType: StateFlow<BottomSheetContentType>
        get() = _currentBottomSheetContentType

    fun setCurrentBottomSheetType(value: BottomSheetContentType){
        _currentBottomSheetContentType.value = value
    }

    private val _collapseBottomSheet = MutableStateFlow(false)
    val collapseBottomSheet: StateFlow<Boolean>
        get() = _collapseBottomSheet

    fun setCollapse(value: Boolean){
        _collapseBottomSheet.value = value
    }

    private val homeUseCaseFactory: HomeUseCaseFactory by inject()
    private val appointmentUseCaseFactory:AppointmentsUseCaseFactory by inject()

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
    val changeMade: StateFlow<Boolean>
        get() = _changeMade

    private val _sessionChangeMade = MutableStateFlow(false)
    val sessionChangeMade: StateFlow<Boolean>
        get() = _sessionChangeMade

    fun refreshEventSelection(){
        _changeMade.value = !_changeMade.value
    }

    fun refreshSessionSelection(){
        _sessionChangeMade.value = !_sessionChangeMade.value
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

    /** @GetCurrentScheduledSession if available*/

    private val _localSession = MutableStateFlow(emptyList<BookAppointmentResponse>())
    val localSession: MutableStateFlow<List<BookAppointmentResponse>>
        get() = _localSession



    fun getSessionLocal(){
        executeLocalFlowUseCase(
            viewModelScope = viewModelScope,
            useCase = appointmentUseCaseFactory.getBookingLocalUseCase,
            inputValue = Unit,
            callback = { result ->
                _localSession.value = result
                if (result.isNotEmpty()){
                    val current = _mainSheetItems.value.toMutableList()
                    _mainSheetItems.value = current.apply {
                        removeAt(2)
                        add(2, EutiMainSheetItem(
                            "View scheduled Appointment",
                            R.drawable.ic_schedule_session,
                            SheetContentType.SCHEDULE_SESSION
                        ))
                    }
                }
                Log.e("Result-Success", result.toString())
            }, onError = {
                //assign to error variable
                Log.e("Error", it.toString())
            })
    }
    /** @MainSheetItems*/

    private val _mainSheetItems = MutableStateFlow(
        listOf(
        EutiMainSheetItem(
            "Show ongoing Events", R.drawable.ic_equaliser,
            SheetContentType.ONGOING_EVENTS
        ),
        EutiMainSheetItem(
            "Show Upcoming Events",
            R.drawable.ic_ongoing_events,
            SheetContentType.UPCOMING_EVENTS
        ),
        EutiMainSheetItem(
            "Book a session with Therapeutic",
            R.drawable.ic_schedule_session,
            SheetContentType.SCHEDULE_SESSION
        ),
        EutiMainSheetItem(
            "Listen to Podcasts",
            R.drawable.ic_watch_featured,
            SheetContentType.LISTEN_TO_PODCASTS
        )
    )
    )

    val mainSheetItems: StateFlow<List<EutiMainSheetItem>>
        get() = _mainSheetItems


    /** Cancel booking */
    private val _cancelBooking = MutableStateFlow(OnResultObtained<BookAppointmentResponse>(null, false))
    val cancelBooking: StateFlow<OnResultObtained<BookAppointmentResponse>>
        get() = _cancelBooking

    fun cancelBooking(bookingId:String){
        executeApiCallUseCase(
            viewModelScope = viewModelScope,
            useCase = appointmentUseCaseFactory.cancelBookingUseCase,
            inputValue = bookingId,
            callback = { result ->
                cancelBookingLocal(result)

            },
            onError = {

            })
    }

    private fun cancelBookingLocal(booking:BookAppointmentResponse){
        executeLocalUseCase(
            viewModelScope = viewModelScope,
            useCase = appointmentUseCaseFactory.cancelBookingLocalUseCase,
            inputValue = Unit,
            callback = { result ->
                val current = _mainSheetItems.value.toMutableList()
                _mainSheetItems.value = current.apply {
                    removeAt(2)
                    add(2, EutiMainSheetItem(
                        "Book a session with Therapeutic",
                        R.drawable.ic_schedule_session,
                        SheetContentType.SCHEDULE_SESSION
                    ))
                }
                _cancelBooking.value = OnResultObtained(booking,true)
                setCurrentBottomSheetType(BottomSheetContentType.EVENT)
                _collapseBottomSheet.value = true
            },
            onError = {
            })
    }

    private val _sessionTherapist = MutableStateFlow(emptyList<TeamMembersItem>())
    val sessionTherapist: StateFlow<List<TeamMembersItem>>
        get() = _sessionTherapist

    fun getTeamembersLocal(id:String) {
        executeLocalFlowUseCase(
            viewModelScope = viewModelScope,
            useCase = appointmentUseCaseFactory.getTeamMembersLocalUseCase,
            inputValue = Unit,
            callback = { result ->
                Log.e("ResultTeam $id",result.toString())
                _sessionTherapist.value = result.filter { tm -> tm.id == id }
            }, onError = {
                //assign to error variable
                it.printStackTrace()
                Log.e("Error", it.toString())
            })
    }


}