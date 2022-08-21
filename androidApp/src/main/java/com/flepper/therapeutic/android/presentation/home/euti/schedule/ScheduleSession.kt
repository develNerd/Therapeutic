package com.flepper.therapeutic.android.presentation.home.euti.schedule

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.databinding.ScheduleSessionCalendarViewBinding
import com.flepper.therapeutic.android.presentation.home.euti.EutiChatType
import com.flepper.therapeutic.android.presentation.home.euti.EutiScreens
import com.flepper.therapeutic.android.presentation.home.euti.EutiViewModel
import com.flepper.therapeutic.android.presentation.theme.*
import com.flepper.therapeutic.android.presentation.widgets.MediumTextBold
import com.flepper.therapeutic.android.presentation.widgets.RoundedCornerButton
import com.flepper.therapeutic.android.util.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

@Composable
fun SelectDateScreen(navController: NavController, eutiViewModel: EutiViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing2dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "")
            }
            MediumTextBold(text = stringResource(id = R.string.select_date), modifier = Modifier.align(
                Alignment.Center))
        }

        val isDark = isSystemInDarkTheme()
        AndroidViewBinding(factory = ScheduleSessionCalendarViewBinding::inflate) {
            val tomorrow =
                Calendar.getInstance()
            calendarView.apply {

                Log.e("Tomorrow", currentDate.toString())
                val date = CalendarDay.from(
                    tomorrow.get(Calendar.YEAR),
                    tomorrow.get(Calendar.MONTH) + 1,
                    tomorrow.get(Calendar.DATE)
                )
                eutiViewModel.setAppointmentDate(date)
                setDateSelected(
                    CalendarDay.from(
                        tomorrow.get(Calendar.YEAR),
                        tomorrow.get(Calendar.MONTH) + 1,
                        tomorrow.get(Calendar.DATE)
                    ), true
                )
                this.setDateTextAppearance(if (isDark) R.color.transGray else R.color.textColor)
                addDecorator(
                    DayDisableDecorator(
                        eutiViewModel.getAvailableDates(),
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_disabled_color,
                            context.theme
                        )!!
                    )
                )
                setOnDateChangedListener { widget, date, selected ->
                    eutiViewModel.setAppointmentDate(date)
                }
            }
        }
        RoundedCornerButton(
            text = stringResource(id = R.string.continue_button),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing5dp, end = spacing5dp, bottom = spacing5dp)
        ) {
            val eutiChat = EutiChatType.Euti(context.getString(R.string.select_appointment_time,"${eutiViewModel.selectedAppointmentDate.value}"), false).apply {
                this.isHead = eutiViewModel.checkHead(this)
            }
            eutiViewModel.addToReplies(eutiChat)
            eutiViewModel.getTeamMembersAvailableTime()
            navController.navigate(EutiScreens.ScheduleSessionTimeScreen(eutiViewModel).screenName)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SelectScheduleTime(navController: NavController, eutiViewModel: EutiViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = spacing5dp),
        verticalArrangement = Arrangement.spacedBy(spacing5dp)
    ) {
        Box() {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "")
            }
        }
        val context = LocalContext.current
        val appointmentTimes = eutiViewModel.appointmentTimes.collectAsState().value.serializeToTimeListString()


        val currentAvailableTime by eutiViewModel.selectedAppointmentTime.collectAsState()


        Column(modifier = Modifier
            .height(280.dp)
            .padding(horizontal = spacing12dp), verticalArrangement = Arrangement.spacedBy(
            mediumPadding)){
            appointmentTimes.forEachIndexed { index, item ->
                AvailableTimeItem(availableTime = item,currentAvailableTime == item){current ->
                    eutiViewModel.setSelectedAppointmentTime(current)
                }
            }
        }
        val isChatLoading by eutiViewModel.isChatLoading.collectAsState()
        RoundedCornerButton(
            text = stringResource(id = R.string.continue_button),
            isEnabled = currentAvailableTime.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing5dp, end = spacing5dp, bottom = spacing5dp)
        ) {

        }


    }
}

@Composable
fun AvailableTimeItem(availableTime: String,isSelected:Boolean,onItemSelected:(String) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(
                if (!isSelected) transGray else MaterialTheme.colors.primaryVariant,
                RoundedCornerShape(5)
            )
            .clickable { onItemSelected(availableTime) }
            .padding(horizontal = mediumPadding, vertical = mediumPadding), contentAlignment = Alignment.Center
    ) {
        MediumTextBold(text = availableTime, textSize = large_bold_text_size)
    }
}
