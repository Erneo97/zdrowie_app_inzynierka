package com.example.firstcomposeap.ui.components

import android.app.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.util.Calendar

@Composable
fun CalendarDialoge(
    baseDate: String = getFormOnlyDate(getCurrentDate()),
    onDateSelected: (String) -> Unit,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    todayOnly : Boolean = true
) {
    val context = LocalContext.current

    if (showDialog) {
        val initialDate = try {
            LocalDate.parse(baseDate)
        } catch (e: Exception) {
            LocalDate.now()
        }

        val calendar = Calendar.getInstance().apply {
            set(initialDate.year, initialDate.monthValue - 1, initialDate.dayOfMonth)
        }

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                onDateSelected(selectedDate.toString())
                onDismiss()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.setOnCancelListener {
            onDismiss()
        }

        datePickerDialog.datePicker.minDate =
            Calendar.getInstance().apply { set(1900, 0, 1) }.timeInMillis
        if( todayOnly)
            datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        else
            datePickerDialog.datePicker.maxDate =  Calendar.getInstance().apply { set(
                calendar.get(Calendar.YEAR) + 3,
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)) }.timeInMillis

        datePickerDialog.show()
    }
}
