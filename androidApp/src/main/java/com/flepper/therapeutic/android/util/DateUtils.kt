package com.flepper.therapeutic.android.util

import android.graphics.drawable.Drawable
import android.util.Log
import com.flepper.therapeutic.data.SQUARE_API_DATE_FORMAT
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toInstant
import java.text.SimpleDateFormat
import java.util.*

fun LocalDateTime.parseToMonthDayString(): String {
    val dateFormat = SimpleDateFormat(
        "EEE, MMM d",
        Locale.getDefault()
    )
    val date = Date(this.toInstant(currentSystemDefault()).toEpochMilliseconds())
    return dateFormat.format(date)
}

fun Calendar.parseToMonthDayString(): String {
    val dateFormat = SimpleDateFormat(
        "EEE, MMM d",
        Locale.getDefault()
    )
    val date = this.time
    return dateFormat.format(date)
}

fun CalendarDay.toAppointmentStartCalendar():Calendar{
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR,this.year)
    cal.set(Calendar.MONTH,this.month - 1)
    cal.set(Calendar.DATE,this.day)
    cal.set(Calendar.HOUR_OF_DAY,8)
    cal.set(Calendar.MINUTE,0)
    cal.set(Calendar.SECOND,0)
    return cal
}

fun CalendarDay.toAppointmentEndCalendar():Calendar{
    val gc = GregorianCalendar()
    gc.set(GregorianCalendar.DATE,this.day)
    gc.add(Calendar.DATE, 1)
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR,this.year)
    cal.set(Calendar.MONTH,this.month - 1)
    cal.set(Calendar.DATE,gc.get(GregorianCalendar.DATE))
    cal.set(Calendar.HOUR_OF_DAY,8)
    cal.set(Calendar.MINUTE,0)
    cal.set(Calendar.SECOND,0)
    return cal
}

fun Date.asSquareApiDateString():String{
   return  SimpleDateFormat(SQUARE_API_DATE_FORMAT, Locale.getDefault()).format(this)
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
    val date = Date(this.toInstant(kotlinx.datetime.TimeZone.currentSystemDefault()).toEpochMilliseconds())
    return dateFormat.format(date)
}

fun Calendar.parseToHourMinuteString(): String {
    val dateFormat = SimpleDateFormat(
        "hh:mm aa",
        Locale.getDefault()
    )
    val date = this.time
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

fun String.convertUTCTimeToSystemDefault():Calendar{
    return try {
        val sourceFormat = SimpleDateFormat(SQUARE_API_DATE_FORMAT, Locale.getDefault())
        sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
        val parsed = sourceFormat.parse(this) // => Date is in UTC now, which is default business time

        val tz = TimeZone.getDefault()
        val destFormat = SimpleDateFormat(SQUARE_API_DATE_FORMAT, Locale.getDefault())
        destFormat.timeZone = tz
        val resultString = destFormat.format(parsed!!)
        val cal =  Calendar.getInstance()
        cal.time = destFormat.parse(resultString)!!
        cal
    }catch (e:Exception){
        e.printStackTrace()
        Log.e("Date",e.message.toString())
        Calendar.getInstance()
    }

}

fun String.fromApiTime():Calendar{
    return try {
        val cal = Calendar.getInstance()
        cal.time = SimpleDateFormat(SQUARE_API_DATE_FORMAT, Locale.getDefault()).parse(this)
        cal
    } catch (e:Exception){
        Calendar.getInstance()
    }
}
