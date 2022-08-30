package com.flepper.therapeutic.android.presentation.home.euti

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.flepper.therapeutic.android.BuildConfig
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.di.ApplicationContext
import com.flepper.therapeutic.android.presentation.core.BaseViewModel
import com.flepper.therapeutic.android.presentation.theme.eventColors
import com.flepper.therapeutic.android.util.*
import com.flepper.therapeutic.data.*
import com.flepper.therapeutic.data.apppreference.AppPreference
import com.flepper.therapeutic.data.models.Filter
import com.flepper.therapeutic.data.models.WorldWideEvent
import com.flepper.therapeutic.data.models.appointments.*
import com.flepper.therapeutic.data.models.appointments.booking.AppointmentSegmentsItem
import com.flepper.therapeutic.data.models.appointments.booking.BookAppointmentResponse
import com.flepper.therapeutic.data.models.appointments.booking.Booking
import com.flepper.therapeutic.data.models.appointments.booking.BookingRequest
import com.flepper.therapeutic.data.models.customer.Customer
import com.flepper.therapeutic.data.models.customer.ReferenceId
import com.flepper.therapeutic.data.usecasefactories.AppointmentsUseCaseFactory
import com.flepper.therapeutic.data.usecasefactories.AuthUseCaseFactory
import com.flepper.therapeutic.data.usecasefactories.HomeUseCaseFactory
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

class EutiViewModel : BaseViewModel() {

    val appPreferences: AppPreference by inject()

    /** Get Whole Application Context -> PS this is okay to get in viewmodel since it doesn't relate to any view
     * Might make testing difficult though bu we'll always find a way
     * */
    val applicationContext: ApplicationContext by inject()
    private val homeUseCaseFactory: HomeUseCaseFactory by inject()
    private val authUseCaseFactory: AuthUseCaseFactory by inject()
    private val appointmentsUseCaseFactory: AppointmentsUseCaseFactory by inject()

    /** @MainBottomSheetItems*/


    /** Euti Replies*/
    private val _eutiReplies = MutableStateFlow(
        listOf<EutiChatType>(
            EutiChatType.Euti("Hi there ${appPreferences.anonUser?.userName}", true),
            EutiChatType.Euti("How can I help you today ?", false)
        )
    )
    val eutiReplies: StateFlow<List<EutiChatType>> = _eutiReplies


    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading

    fun setIsChatLoading(value: Boolean) {
        _isChatLoading.value = value
    }

    private val _isChatAdded = MutableStateFlow(false)
    val isChatAdded: StateFlow<Boolean>
        get() = _isChatAdded

    fun setIsChatAdded(value: Boolean) {
        _isChatAdded.value = value
    }

    /** @AddReply*/
    fun addToReplies(chatType: EutiChatType) {
        var replies = mutableListOf<EutiChatType>()
        replies = eutiReplies.value.toMutableList()
        replies.add(chatType)
        _eutiReplies.value = replies.toList()
        setIsChatAdded(true)

    }

    /** @LogicTODetermineHead */
    fun checkHead(eutiChatType: EutiChatType): Boolean {
        when (eutiChatType) {
            is EutiChatType.Euti -> {
                return _eutiReplies.value.last() !is EutiChatType.Euti
            }
            is EutiChatType.Content -> {}
            is EutiChatType.User -> {
                return _eutiReplies.value.last() !is EutiChatType.User
            }
        }
        return false
    }


    /** @GenericScreenTitle*/
    private val _genericTitle = MutableStateFlow("")
    val genericTitle: StateFlow<String>
        get() = _genericTitle

    fun setGenericTitle(value: String) {
        _genericTitle.value = value
    }

    /** @GetEvents this logic exists in the homeviewModel, but we'll like that loading feature*/
    private val _events = MutableStateFlow(OnResultObtained<List<WorldWideEvent>>(null, false))
    val events: MutableStateFlow<OnResultObtained<List<WorldWideEvent>>>
        get() = _events

    /** @UpcomingEvts this logic exists in the homeviewModel, but we'll like that loading feature*/
    private val _eventsUpcoming =
        MutableStateFlow(OnResultObtained<List<WorldWideEvent>>(null, false))
    val eventsUpcoming: MutableStateFlow<OnResultObtained<List<WorldWideEvent>>>
        get() = _eventsUpcoming

    /** TODO -> Also Write UseCse To Get Only Ongoing Events, Currently filtering because of time and events are not so much so that's alright */
    fun getOnGoingEvents(isOngoing: Boolean) {
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
                    if (backgroundColors.contains(itemColor)) {
                        backgroundColors.remove(itemColor)
                    }
                    worldWideEvent.backGroundColor = itemColor
                    if (currentColorIndex < backgroundColors.size - 1) currentColorIndex++ else currentColorIndex =
                        0
                    worldWideEvent
                }
                Log.e("Result", eventResult.toString())
                /** Assuming on Going events */
                val eutiListReply =
                    if (eventResult.isEmpty()) applicationContext().getString(R.string.events_not_available) else applicationContext().getString(
                        R.string.response_to_found_events
                    )
                val eutiOnGoingEventReply =
                    if (eventResult.isEmpty()) applicationContext().getString(R.string.events_not_available) else applicationContext().getString(
                        R.string.response_to_ongoing_event,
                        eventResult.firstOrNull()?.hashTag ?: ""
                    )
                val eutiReplyText = if (!isOngoing) eutiListReply else eutiOnGoingEventReply


                addToReplies(
                    EutiChatType.Euti(eutiReplyText, true).apply { this.isHead = checkHead(this) })
                addToReplies(
                    EutiChatType.Content(
                        _currentEutiType.value.id,
                        eventResult.filter { if (isOngoing) it.isOngoing else !it.isOngoing },
                        listOf(),
                        _currentEutiType.value.sheetContentType
                    )
                )
                setIsChatLoading(false)
            }, onError = {
                //assign to error variable
            })
    }

    /** Current OptionBottom Id ]*/
    private val _currentOptionID = MutableStateFlow("")
    val currentOptionID: StateFlow<String>
        get() = _currentOptionID

    fun setOptionBottomId(value: String) {
        _currentOptionID.value = value
    }

    /** @CurrenEutiType*/

    private val _currentEutiType = MutableStateFlow(
        EutiChatType.Content(
            "", listOf(),
            emptyList(), SheetContentType.DEFAULT
        )
    )
    val currentEutiType: StateFlow<EutiChatType>
        get() = _currentEutiType

    fun setCurrentEutiType(value: EutiChatType) {
        _currentEutiType.value = value as EutiChatType.Content
    }

    /** @SignInOrLogin - To book a session*/
    enum class SignInMethod {
        SIGN_IN,
        SIGN_UP
    }

    var signInMethod = SignInMethod.SIGN_UP

    /** @SignIn*/

    private val _signUpResponse =
        MutableStateFlow(OnResultObtained<CurrentUser>(null, false))
    val signUpResponse: StateFlow<OnResultObtained<CurrentUser>>
        get() = _signUpResponse


    fun signUpUser(email: String, password: String) {
        _signUpResponse.value = OnResultObtained(null, false)

        val signUpRequest =
            SignUpRequest(appPreferences.anonUser?.userName ?: "Anon", email, password)
        executeFirebaseUseCase(
            viewModelScope = viewModelScope,
            inputValue = signUpRequest,
            useCase = authUseCaseFactory.signUpUseCase,
            state = _signUpResponse,
            callback = { result ->
                Log.e("Result", result.toString())
                getCustomer(result.id, result, SignInMethod.SIGN_UP)
            }, onError = {
                //assign to error variable
                _eutiGenericError.value =
                    applicationContext().getString(R.string.something_went_wrong)

            })
    }


    /** @SignIn */
    private val _signInResponse =
        MutableStateFlow(OnResultObtained<CurrentUser>(null, false))
    val signInResponse: StateFlow<OnResultObtained<CurrentUser>>
        get() = _signInResponse

    fun signInUser(email: String, password: String) {
        _signInResponse.value = OnResultObtained(null, false)

        val signInRequest =
            SignInRequest(email, password)
        executeFirebaseUseCase(
            viewModelScope = viewModelScope,
            inputValue = signInRequest,
            useCase = authUseCaseFactory.signInUseCase,
            state = _signInResponse,
            callback = { result ->
                Log.e("Result", result.toString())
                _signInResponse.value = OnResultObtained(result, true)
                appPreferences.signInUser = result
            }, onError = {
                //assign to error variable
                _eutiGenericError.value =
                    applicationContext().getString(R.string.something_went_wrong)
            })
    }

    fun signInWithGoogle(idToken: String) {
        executeFirebaseUseCase(
            viewModelScope = viewModelScope,
            inputValue = idToken,
            useCase = authUseCaseFactory.signInWithGoogleUseCase,
            state = _signInResponse,
            callback = { result ->
                Log.e("Result", result.toString())
                // _signInResponse.value = OnResultObtained(result, true)
                getCustomer(result.id, result, SignInMethod.SIGN_IN)
            }, onError = {
                //assign to error variable
                _eutiGenericError.value =
                    applicationContext().getString(R.string.something_went_wrong)
            })
    }

    /** @GetCustomer*/

    /** Get Customer if customer does not exist and return if exists */
    private fun getCustomer(
        referenceId: String,
        signInResult: CurrentUser,
        signInMethod: SignInMethod
    ) {
        val request = Filter(referenceId = ReferenceId(referenceId))
        executeApiCallUseCase(
            viewModelScope = viewModelScope,
            useCase = appointmentsUseCaseFactory.getCustomerUseCase,
            inputValue = request,
            callback = { resultList ->
                val result = resultList.first()
                Log.e("Result-Cust", result.toString())
                if (signInMethod == SignInMethod.SIGN_IN) {
                    _signInResponse.value = OnResultObtained(
                        signInResult.apply { squareCustomerID = result.customer_id },
                        true
                    )
                    appPreferences.signInUser = signInResult.apply { squareCustomerID = result.customer_id }
                } else {
                    _signUpResponse.value = OnResultObtained(signInResult, true)
                    appPreferences.signInUser = signInResult.apply { squareCustomerID = result.customer_id }
                }

            },
            onError = {
                /** Create if customer does not exist*/
                createCustomer(referenceId, signInResult, signInMethod)
                Log.e("Result", it.message.toString())
            })
    }

    /** @CreateCustomer */

    fun createCustomer(referenceId: String, signInResult: CurrentUser, signInMethod: SignInMethod) {
        val request = Customer(
            signInResult.email,
            referenceId,
            COMPANY_NAME,
            DEFAULT_NOTE,
            appPreferences.anonUser?.userName ?: "",
            appPreferences.anonUser?.userName ?: "",
            COMPANY_PHONE_NUMBER
        )
        executeApiCallUseCase(
            viewModelScope = viewModelScope,
            useCase = appointmentsUseCaseFactory.createCustomerUseCase,
            inputValue = request,
            callback = { result ->
                if (signInMethod == SignInMethod.SIGN_IN) {
                    _signInResponse.value = OnResultObtained(
                        signInResult.apply { squareCustomerID = result.customer_id },
                        true
                    )
                    appPreferences.signInUser = signInResult
                } else {
                    _signUpResponse.value = OnResultObtained(signInResult, true)
                    appPreferences.signInUser = signInResult
                }
            },
            onError = {
                _eutiGenericError.value =
                    applicationContext().getString(R.string.something_went_wrong)
                Log.e("Result", it.message.toString())
            })
    }

    /** @SignInError*/
    private val _eutiGenericError = MutableStateFlow("")
    val eutiGenericError: StateFlow<String>
        get() = _eutiGenericError

    fun setSignInError(value: String) {
        _eutiGenericError.value = value
    }

    /** @SetAppointmentDate*/
    private val _selectedAppointmentDate = MutableStateFlow(CalendarDay.today())
    val selectedAppointmentDate: StateFlow<CalendarDay>
        get() = _selectedAppointmentDate

    fun setAppointmentDate(value: CalendarDay) {
        _selectedAppointmentDate.value = value
    }

    /** @AvailableDates Local*/

    fun getAvailableDates(): HashSet<CalendarDay> {
        val availableDates = mutableSetOf<CalendarDay>()
        val startCal = Calendar.getInstance()
        var start = startCal.get(Calendar.DATE)
        startCal.set(Calendar.DATE, startCal.get(Calendar.DATE).plus(7))
        val lastIndex = startCal.get(Calendar.DATE)
        var isEndOfMonth = false

        if (start > lastIndex){
            isEndOfMonth = true
            availableDates.add(Calendar.getInstance().toCalendarDay())
            start = 1
        }
        Log.e("Start-last", "$start + $lastIndex")

        (start..lastIndex).forEach { day ->

            val c1 = if (!isEndOfMonth) Calendar.getInstance() else Calendar.getInstance().apply { this.set(Calendar.MONTH,this.get(Calendar.MONTH) + 1) }
            c1.set(Calendar.DATE, day)
            availableDates.add(c1.toCalendarDay())
        }

        return availableDates.toHashSet()
    }

    /** @GetAvailableTimes Server*/

    fun getTeamMembersAvailableTime() {
        executeLocalFlowUseCase(
            viewModelScope = viewModelScope,
            useCase = appointmentsUseCaseFactory.getTeamMembersLocalUseCase,
            inputValue = Unit,
            callback = { result ->
                Log.e("Result-Success", result.toString())
                val startAt =
                    _selectedAppointmentDate.value.toAppointmentStartCalendar().time.asSquareApiDateString()
                val endAt =
                    _selectedAppointmentDate.value.toAppointmentEndCalendar().time.asSquareApiDateString()
                val startAtRange = StartAtRange(startAt = startAt, endAt = endAt)
                getAvailableTimes(startAtRange, result.map { it.id })
            }, onError = {
                //assign to error variable
                it.printStackTrace()
                Log.e("Error", it.toString())
            })
    }

    val teamMemberTimeAndIdPair: MutableMap<String, String> = mutableMapOf()

    private fun getAvailableTimes(
        startAtRange: StartAtRange,
        teamMemberIds: List<String>
    ) {
        val segmentFilters = SegmentFiltersItem(
            serviceVariationId = BuildConfig.THERAPY_SESSION_CATALOG_ITEM_ID,
            teamMemberIdFilter = TeamMemberIdFilter(teamMemberIds)
        )
        val appointmentsFilter = AppointmentsFilter(
            bookingId = "",
            startAtRange,
            listOf(segmentFilters),
            locationId = BuildConfig.DEFAULT_TEST_LOCATION_ID
        )
        val request = SearchAvailabilityRequest(AvailabilityQuery(appointmentsFilter))
        executeApiCallUseCase(
            viewModelScope = viewModelScope,
            useCase = appointmentsUseCaseFactory.getAvailableTimeUseCase,
            inputValue = request,
            callback = { availableTimes ->
                val availableTimePair: MutableMap<String, String> = mutableMapOf()
                availableTimes.forEach { availableTime ->
                    val hourMinuteFormat = SimpleDateFormat(
                        "hh:mm a",
                        Locale.getDefault()
                    )
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = availableTime.convertStartToUserTimeZone()
                    /** Filter for if minute is 30*/
                    if (cal.get(Calendar.MINUTE) != 30) {
                        val c1 = availableTime.startAt.convertUTCTimeToSystemDefault()
                        val end = availableTime.startAt.convertUTCTimeToSystemDefault()
                        // set duration for end
                        end.set(
                            Calendar.MINUTE,
                            availableTime.appointmentSegments?.first()?.durationMinutes ?: 45
                        )
                        val c2 = end
                        availableTimePair[hourMinuteFormat.format(c1.time)] =
                            hourMinuteFormat.format(c2.time)
                        teamMemberTimeAndIdPair[hourMinuteFormat.format(c1.time)] =
                            availableTime.appointmentSegments?.first()?.teamMemberId ?: ""
                    }
                }
                _appointmentTimes.value = availableTimePair
                selectedAppointmentDate.value.apply {
                    val eutiChat = EutiChatType.Euti(applicationContext().getString(R.string.select_appointment_time,"$year-$month-$day"), false).apply {
                        this.isHead = checkHead(this)
                    }
                    addToReplies(eutiChat)
                }
                setIsChatLoading(false)
            },
            onError = {
                _eutiGenericError.value =
                    applicationContext().getString(R.string.something_went_wrong)
            })
    }


    /** @AvailableTimes*/
    private val _appointmentTimes = MutableStateFlow(mapOf<String, String>())
    val appointmentTimes: StateFlow<Map<String, String>>
        get() = _appointmentTimes

    /** Not in use anymore*/

    fun getAvailableTimesLocalTest() {
        val availableTimePair: MutableMap<String, String> = mutableMapOf()
        val dateFormat = SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        )
        try {
            (8..17).forEach { start ->
                val c1 = Calendar.getInstance()
                val timeOfDay = if (start < 12) "am" else "pm"
                c1.time = dateFormat.parse("$start:00 $timeOfDay")!!

                val c2 = Calendar.getInstance()
                c2.time = dateFormat.parse("$start:45 $timeOfDay")!!
                availableTimePair[dateFormat.format(c1.time)] = dateFormat.format(c2.time)
            }
        } catch (e: Exception) {
            Log.e("", "")
        }

        _appointmentTimes.value = availableTimePair
    }


    /** @SelectedAppointmentTime*/

    private var selectedTeamberId = ""


    private val _selectedAppointmentTime = MutableStateFlow("")
    val selectedAppointmentTime: MutableStateFlow<String>
        get() = _selectedAppointmentTime

    fun setSelectedAppointmentTime(value: String) {
        _selectedAppointmentTime.value = value
    }

    /** @BookAppointment Finally Book an Appointment*/
    fun bookAppointment() {
        val timeKey = _selectedAppointmentTime.value.split("-").first().toString().trim()

        /** timekey is expected to be 8:00 am*/
        val startAt = toSquareApiStartAt(_selectedAppointmentDate.value, timeKey)

        Log.e("StartAt",startAt)

        val teamMemberId = teamMemberTimeAndIdPair[timeKey]
        val appointmentSegment = AppointmentSegmentsItem(
            DEFAULT_SESSION_DURATION,
            teamMemberId ?: "",
            DEFAULT_SERVICE_VERSION,
            BuildConfig.THERAPY_SESSION_CATALOG_ITEM_ID
        )
        val booking = Booking(
            listOf(appointmentSegment),
            DEFAULT_CUSTOMER_NOTE,
            appPreferences.signInUser?.squareCustomerID ?: "",
            startAt,
            DEFAULT_LOCATION_TYPE,
            BuildConfig.DEFAULT_TEST_LOCATION_ID
        )
        val request = BookingRequest(booking)
        executeApiCallUseCase(
            viewModelScope = viewModelScope,
            useCase = appointmentsUseCaseFactory.bookAppointmentUseCase,
            inputValue = request,
            callback = { result ->
                setIsChatLoading(false)
                saveBookingLocal(result)
                addToReplies(EutiChatType.Euti(applicationContext().getString(R.string.yay_booked),false).apply {
                    isHead = checkHead(this)
                })
            },
            onError = {
                setIsChatLoading(false)
                _eutiGenericError.value =
                    applicationContext().getString(R.string.something_went_wrong)
                addToReplies(EutiChatType.Euti(applicationContext().getString(R.string.euti_sorry_something_went_wrong),false).apply {
                    isHead = checkHead(this)
                })
            })
    }


    /** @SaveBookingLocal*/
    private val _bookingSuccess = MutableStateFlow(false)

    val bookingSuccess: MutableStateFlow<Boolean>
        get() = _bookingSuccess

    fun setBookingFailed(){
        _bookingSuccess.value = false
    }

    fun saveBookingLocal(request: BookAppointmentResponse){
        executeLocalUseCase(
            viewModelScope = viewModelScope,
            useCase = appointmentsUseCaseFactory.saveBookingLocal,
            inputValue = request,
            callback = { result ->
                Log.e("Result-Success", "Yay")
                _bookingSuccess.value = true
                addToReplies(EutiChatType.Content(UUID.randomUUID().toString(),
                    listOf(), listOf(request),SheetContentType.SCHEDULE_SESSION))
            }, onError = {
                //assign to error variable
                setBookingFailed()
                it.printStackTrace()
                Log.e("Error", it.toString())
            })
    }

    private fun toSquareApiStartAt(calendarDay: CalendarDay, startTime: String): String {
        val startAtCal = Calendar.getInstance()
        startAtCal.set(Calendar.YEAR, calendarDay.year)
        startAtCal.set(Calendar.MONTH, calendarDay.month - 1)
        startAtCal.set(Calendar.DATE, calendarDay.day)
        val pm = startTime.split(" ")[1].trim().lowercase()
        startAtCal.set(Calendar.AM_PM,if (pm.contains(pm)) Calendar.PM else Calendar.AM)
        startAtCal.set(Calendar.HOUR, startTime.split(":").first().trim().toInt())
        //
        startAtCal.set(Calendar.MINUTE, 0)
        startAtCal.set(Calendar.SECOND, 0)
        startAtCal.set(Calendar.MILLISECOND, 0)

        val sourceFormat = SimpleDateFormat(SQUARE_API_DATE_FORMAT, Locale.getDefault())
        return sourceFormat.format(startAtCal.time) // => Date is in UTC now, which is default business time
    }





}