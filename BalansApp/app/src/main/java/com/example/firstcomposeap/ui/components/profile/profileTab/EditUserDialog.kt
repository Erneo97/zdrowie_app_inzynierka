package com.example.firstcomposeap.ui.components.profile.profileTab

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
import androidx.compose.ui.text.input.KeyboardType
import com.example.balansapp.ui.service.data.Uzytkownik


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
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val updatedUser = user.copy(
                    imie = imie,
                    nazwisko = nazwisko,
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


