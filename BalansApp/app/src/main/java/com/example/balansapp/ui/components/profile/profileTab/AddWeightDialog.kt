package com.example.firstcomposeap.ui.components.profile.profileTab

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.balansapp.R
import com.example.balansapp.ui.service.data.PommiarWagii
import com.example.firstcomposeap.ui.components.getCurrentDate


@Composable
fun AddWeightDialog(
    onDismiss: () -> Unit,
    onConfirm: (PommiarWagii) -> Unit
) {
    var waga by remember { mutableStateOf("") }
    var tluszcz by remember { mutableStateOf("") }
    var miesnie by remember { mutableStateOf("") }
    var nawodnienie by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nowy pomiar wagi") },
        text = {
            Column {
                OutlinedTextField(
                    value = waga,
                    onValueChange = { waga = it },
                    label = { Text("Waga (kg)") }
                )
                OutlinedTextField(
                    value = tluszcz,
                    onValueChange = { tluszcz = it },
                    label = { Text("Tłuszcz (%)") }
                )
                OutlinedTextField(
                    value = miesnie,
                    onValueChange = { miesnie = it },
                    label = { Text("Mięśnie (%)") }
                )
                OutlinedTextField(
                    value = nawodnienie,
                    onValueChange = { nawodnienie = it },
                    label = { Text("Nawodnienie (%)") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val currentDate = getCurrentDate()
                    val newPomiar = PommiarWagii(
                        wartosc = waga.replace(",", ".")
                            .toDoubleOrNull() ?: -1.0,
                        data = currentDate,
                        tluszcz = tluszcz.replace(",", ".")
                            .toDoubleOrNull() ?: 0.0,
                        miesnie = miesnie.replace(",", ".")
                            .toDoubleOrNull() ?: 0.0,
                        nawodnienie = nawodnienie.replace(",", ".")
                            .toDoubleOrNull() ?: 0.0
                    )
                    if( newPomiar.wartosc != -1.0)
                        onConfirm(newPomiar)
                    else
                        Toast.makeText(context, context.getString(R.string.bad_add_weight_form), Toast.LENGTH_SHORT).show()
                }
            ) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}



