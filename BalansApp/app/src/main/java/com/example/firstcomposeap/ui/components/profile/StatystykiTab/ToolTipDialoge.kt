package com.example.firstcomposeap.ui.components.profile.StatystykiTab

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ToolTipDialoge(
    onConfirm: (zapotrzebowanie: Int) -> Unit,
    text: String
) {
    var kcal by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { },
        title = {
            Text("A to przydatne", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Text(text)
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(kcal.toIntOrNull() ?: 0)
            }) {
                Text("Rozumiem")
            }
        },
    )
}
