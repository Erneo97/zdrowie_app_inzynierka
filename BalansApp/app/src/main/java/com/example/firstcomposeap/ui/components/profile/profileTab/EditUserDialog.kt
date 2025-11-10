package com.example.firstcomposeap.ui.components.profile.profileTab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.balansapp.ui.service.data.Uzytkownik
import com.example.firstcomposeap.ui.components.CalendarDialoge
import com.example.firstcomposeap.ui.components.getFormOnlyDate
import com.example.firstcomposeap.ui.components.icon.Edit_calendar


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

    var showDatePicker by remember { mutableStateOf(false) }
    CalendarDialoge(
        baseDate = dataUrodzenia,
        showDialog = showDatePicker,
        onDateSelected = { dataUrodzenia = it },
        onDismiss = { showDatePicker = false }
    )

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
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    OutlinedTextField(
                        value = dataUrodzenia,
                        onValueChange = { showDatePicker = true },
                        label = { Text("Data urodzenia") },
                        modifier = Modifier.weight(1f)
                            .clickable { showDatePicker = true },
                        readOnly = true
                    )
                    IconButton(onClick = {showDatePicker = true},
                        modifier = Modifier.size(38.dp)) {
                        Icon ( imageVector = Edit_calendar, contentDescription = "wybierz date")
                    }
                }

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


