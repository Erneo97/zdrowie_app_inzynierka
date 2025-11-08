package com.example.firstcomposeap.ui.components.profile.profileTab

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.balansapp.ui.service.data.Uzytkownik
import com.example.firstcomposeap.ui.components.getFormOnlyDate
import java.time.LocalDate
import java.util.Calendar


@Composable
fun EditUserDialog(
    user: Uzytkownik?,
    onDismiss: () -> Unit,
    onConfirm: (Uzytkownik) -> Unit
) {
    if (user == null) return

    var imie by remember { mutableStateOf(user.imie ?: "") }
    var nazwisko by remember { mutableStateOf(user.nazwisko ?: "") }
    var wzrost by remember { mutableStateOf(user.wzrost?.toString() ?: "") }
    var email by remember { mutableStateOf(user.email ?: "") }
    var dataUrodzenia by remember { mutableStateOf(getFormOnlyDate(user.dataUrodzenia ?: "")) } // nowe pole
    val context = LocalContext.current

    val initialDate = try {
        LocalDate.parse(dataUrodzenia)
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
            dataUrodzenia = selectedDate.toString() // yyyy-MM-dd
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    datePickerDialog.datePicker.minDate = Calendar.getInstance().apply { set(1900, 0, 1) }.timeInMillis
    datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Edytuj dane użytkownika") },
        text = {
            Column {
                OutlinedTextField(
                    value = imie,
                    onValueChange = { imie = it },
                    label = { Text("Imię") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nazwisko,
                    onValueChange = { nazwisko = it },
                    label = { Text("Nazwisko") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = wzrost,
                    onValueChange = { wzrost = it },
                    label = { Text("Wzrost (cm)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                OutlinedTextField(
                    value = dataUrodzenia,
                    onValueChange = { datePickerDialog.show() },
                    label = { Text("Data urodzenia") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() },
                    readOnly = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val updatedUser = user.copy(

                    imie = imie,
                    nazwisko = nazwisko,
                    dataUrodzenia = dataUrodzenia,
                    wzrost = wzrost.toIntOrNull() ?: 0,
                    email = email
                )
                onConfirm(updatedUser)
            }) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Anuluj")
            }
        }
    )
}


