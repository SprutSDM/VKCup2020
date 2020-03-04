package ru.zakoulov.vkcupa.ui.main

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateFormatter(
    todayFormat: String,
    yesterdayFormat: String,
    thisYearFormat: String,
    oldYearFormat: String,
    locale: Locale
) {

    private val sdfToday = SimpleDateFormat(todayFormat, locale)
    private val sdfYesterday = SimpleDateFormat(yesterdayFormat, locale)
    private val sdfThisYear = SimpleDateFormat(thisYearFormat, locale)
    private val oldYearFormat = SimpleDateFormat(oldYearFormat, locale)

    fun getFormattedDate(millis: Long): String {
        val date = Date(millis)
        if (DateUtils.isToday(millis)) {
            return sdfToday.format(date).replace(".", "")
        }
        if (DateUtils.isToday(millis + DateUtils.DAY_IN_MILLIS)) {
            return sdfYesterday.format(date).replace(".", "")
        }
        val docDate = Calendar.getInstance()
        docDate.timeInMillis = millis
        val now = Calendar.getInstance()

        if (now[Calendar.YEAR] == docDate[Calendar.YEAR]) {
            return sdfThisYear.format(date).replace(".", "")
        }
        return oldYearFormat.format(date).replace(".", "")
    }
}