package com.example.firstcomposeap.ui.components

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun getCurrentDate() : String {
    val dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return LocalDate.now().format(dateFormater).toString()
}

fun getFormOnlyDate(date : String) : String {
    return if (date.length >= 10) date.substring(0, 10) else date
}

fun calculateUserAge(dataUrodzenia: String): Double {
    val ograniczonaDataUrodzenia = getFormOnlyDate(dataUrodzenia)
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val birthDate = LocalDate.parse(ograniczonaDataUrodzenia, formatter)
        val today = LocalDate.now()
        val period = Period.between(birthDate, today)


        val ageWithFraction = period.years + (period.months / 12.0) + (period.days / 365.0)
        String.format("%.2f", ageWithFraction).toDouble()
    } catch (e: Exception) {
        0.0
    }
}


fun getWeekDayNumbers(dateString: String): List<Int> {
    val date = LocalDate.parse(dateString)
    val dayNumber = date.dayOfWeek.value
    val monday = date.minusDays((dayNumber - 1).toLong())

    return (0..6).map { monday.plusDays(it.toLong()).dayOfMonth }
}