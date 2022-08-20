package com.flepper.therapeutic.android.presentation.home.euti.schedule

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import com.flepper.therapeutic.android.R
import com.flepper.therapeutic.android.databinding.ScheduleSessionCalendarViewBinding
import com.flepper.therapeutic.android.presentation.home.euti.EutiViewModel
import com.flepper.therapeutic.android.presentation.theme.mediumPadding
import com.flepper.therapeutic.android.presentation.theme.spacing2dp
import com.flepper.therapeutic.android.presentation.theme.spacing5dp
import com.flepper.therapeutic.android.presentation.theme.transGray
import com.flepper.therapeutic.android.presentation.widgets.MediumTextBold
import com.flepper.therapeutic.android.presentation.widgets.RoundedCornerButton
import com.flepper.therapeutic.android.util.DayActiveDecorator
import com.flepper.therapeutic.android.util.DayDisableDecorator
import com.flepper.therapeutic.android.util.toCalendarDay
import com.flepper.therapeutic.android.util.toCalendarDayM
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate
import java.util.*

@Composable
fun SelectDateScreen(navController: NavController, eutiViewModel: EutiViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing2dp)
    ) {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "")
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
                setDateSelected(
                    CalendarDay.from(
                        tomorrow.get(Calendar.YEAR),
                        tomorrow.get(Calendar.MONTH) + 1,
                        tomorrow.get(Calendar.DATE)
                    ), true
                )
                this.setDateTextAppearance(if (isDark) R.color.transGray else R.color.transGray)
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
                    eutiViewModel.setAppointmentDate("${date.year}-${date.month}-${date.day}")
                }
            }
        }
        RoundedCornerButton(
            text = stringResource(id = R.string.continue_button),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacing5dp, end = spacing5dp, bottom = spacing5dp)
        ) {
            navController
        }
    }
}

@Composable
fun SelectScheduleTime(navController: NavController, eutiViewModel: EutiViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing5dp)
    ) {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "")
        }

    }
}

@Composable
fun AvailableTimeItem(availableTime: String) {
    Box(
        Modifier
            .background(transGray, RoundedCornerShape(5))
            .padding(horizontal = mediumPadding, vertical = spacing5dp)
    ) {
        MediumTextBold(text = availableTime)

    }
}
