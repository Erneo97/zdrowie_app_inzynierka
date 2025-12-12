package com.example.firstcomposeap.ui.components.treningplans

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstcomposeap.ui.components.icon.Delete
import com.example.firstcomposeap.ui.service.data.Seria
import com.example.firstcomposeap.ui.service.data.cwiczeniaPlanuTreningowego


@Composable
fun CwiczenieSeriesItem(
    cwiczenie: cwiczeniaPlanuTreningowego,
    onRemove: (cwiczeniaPlanuTreningowego) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text =" ${cwiczenie.nazwa}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(2.dp))
        HorizontalDivider()

        Spacer(modifier = Modifier.height(8.dp))


        cwiczenie.serie.forEachIndexed { index, seria ->

            var liczbaPowtorzen by remember(seria) {
                mutableStateOf(seria.liczbaPowtorzen.toString())
            }

            var obciazenie by remember(seria) {
                mutableStateOf(
                    if (seria.obciazenie == 0f) "" else seria.obciazenie.toString()
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {

                Text("${index + 1}", modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.width(2.dp))

                TextField(
                    value = liczbaPowtorzen,
                    onValueChange = {
                        liczbaPowtorzen = it
                        seria.liczbaPowtorzen = it.toIntOrNull() ?: 0
                    },
                    label = { Text("Powtórzenia") },
                    modifier = Modifier.weight(5f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = obciazenie,
                    onValueChange = {
                        obciazenie = it
                        seria.obciazenie = it.toFloatOrNull() ?: 0f
                    },
                    label = { Text("Obciążenie") },
                    modifier = Modifier.weight(4f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                IconButton(
                    onClick = { cwiczenie.serie.remove(seria) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Delete, contentDescription = "Usuń serię")
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val lastSerie = cwiczenie.serie.lastOrNull()
                    val newSerie = Seria(
                        liczbaPowtorzen = lastSerie?.liczbaPowtorzen ?: 0,
                        obciazenie = lastSerie?.obciazenie ?: 0f
                    )
                    cwiczenie.serie.add(newSerie)
                },
                modifier = Modifier.weight(7f)
            ) {
                Text("Dodaj serię")
            }

            Spacer(modifier = Modifier.weight(1f)) // przesunięcie ikony w prawo

            IconButton(
                onClick = { onRemove(cwiczenie) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Delete,
                    contentDescription = "Usuń ćwiczenie"
                )
            }
        }
    }

}
