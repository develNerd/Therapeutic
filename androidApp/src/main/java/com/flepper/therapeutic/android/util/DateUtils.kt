package com.flepper.therapeutic.android.util

import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.text.SimpleDateFormat
import java.util.*

fun LocalDateTime.parseToMonthDayString(): String {
    val dateFormat = SimpleDateFormat(
        "EEE, MMM d",
        Locale.getDefault()
    )
    val date = Date(this.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds())
    return dateFormat.format(date)
}

fun Date.toServerTimeString():String{
    val dateFormat = SimpleDateFormat(
        "yyyy-MM-DD",
        Locale.getDefault()
    )
    return dateFormat.format(this)
}

fun LocalDateTime.parseToHourMinuteString(): String {
    val dateFormat = SimpleDateFormat(
        "hh:mm aa",
        Locale.getDefault()
    )
    val date = Date(this.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds())
    return dateFormat.format(date)
}

class DayDisableDecorator(
    private val dates: HashSet<CalendarDay>,
    private val disabledBackground: Drawable
) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return !dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.setBackgroundDrawable(disabledBackground)
        view?.setDaysDisabled(true)
    }
}

class DayActiveDecorator(
    private val dates: HashSet<CalendarDay>,
    private val activeBackground: Drawable
) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return !dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.setBackgroundDrawable(activeBackground)
        view?.setDaysDisabled(false)

    }
}

fun Calendar.toCalendarDay():CalendarDay{

    return CalendarDay.from(get(Calendar.YEAR),get(Calendar.MONTH) + 1,get(Calendar.DATE))
}

fun Calendar.toCalendarDayM():CalendarDay{

    return CalendarDay.from(2022,8,20)
}


fun  Map<String, String>.serializeToTimeListString():List<String>{
    val items = mutableListOf<String>()
    this.forEach { (start, end)->
        items.add("$start - $end")
    }
    return items
}

