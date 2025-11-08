package com.example.firstcomposeap.ui.components

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun getCurrentDate() : String {
    val dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return LocalDate.now().format(dateFormater).toString()
}

fun getFormOnlyDate(date : String) : String {
    return if (date.length >= 10) date.substring(0, 10) else date
}