package com.example.firstcomposeap.ui.components.treningplans

import android.os.SystemClock
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.firstcomposeap.ui.service.data.CwiczenieWTreningu
import com.example.firstcomposeap.ui.service.data.Seria
import kotlinx.coroutines.delay


@Composable
fun CwiczenieTreningItem(
    cwiczenie: CwiczenieWTreningu,
    onRemove: (CwiczenieWTreningu) -> Unit,
    updateTime : (String) -> Unit
) {
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var elapsedMs by remember { mutableStateOf(parseTimeToMs(cwiczenie.czas)) }
    var accumulatedTime by remember { mutableStateOf(parseTimeToMs(cwiczenie.czas)) }


    LaunchedEffect(isRunning) {
        if (isRunning) {
            startTime = SystemClock.uptimeMillis()
            while (isRunning) {
                elapsedMs = accumulatedTime + (SystemClock.uptimeMillis() - startTime)
                delay(400)
                updateTime(formatMMSS((elapsedMs)))
            }
        }
    }


    fun startStopwatch() {
        isRunning = true
    }

    fun pauseStopwatch() {
        isRunning = false
        accumulatedTime = elapsedMs
    }

    fun resetStopwatch() {
        isRunning = false
        elapsedMs = 0L
        accumulatedTime = 0L
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text =" ${cwiczenie.nazwa}",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(2.dp))
        HorizontalDivider()

        Spacer(modifier = Modifier.height(8.dp))
        Text("czas ćwiczenia:  ${formatMMSS(elapsedMs)}", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { resetStopwatch() }, modifier = Modifier.weight(1.3f)
            ) {
                Text("Reset")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { startStopwatch() }, modifier = Modifier.weight(2f)) {
                Text("Start")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { pauseStopwatch() }, modifier = Modifier.weight(2f)) {
                Text("Pauza")
            }

        }

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

            var done by remember(seria) { mutableStateOf(false ) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp)
            ) {
                Text("${index + 1}", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 20.sp)

                Spacer(modifier = Modifier.width(2.dp))

                if( !done ) {
                    TextField(
                        value = liczbaPowtorzen,
                        onValueChange = {
                            liczbaPowtorzen = it
                            seria.liczbaPowtorzen = it.toIntOrNull() ?: 0
                        },
                        label = { Text("Powtórzenia") },
                        modifier = Modifier.weight(4f),
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
                else {
                    Column (modifier = Modifier.weight(4f),) {
                        Text("Powtórzenia")
                        Text(liczbaPowtorzen, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column (modifier = Modifier.weight(4f),) {
                        Text("Obciążenie")
                        Text(obciazenie, fontWeight = FontWeight.SemiBold)
                    }
                }

                Checkbox(
                    checked = done,
                    onCheckedChange = {done = it},
                    modifier = Modifier.weight(2f).padding(2.dp)
                )
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

            // przesunięcie ikony w prawo
            if( elapsedMs == 0L) {
                Spacer(modifier = Modifier.weight(1f))
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

}

fun formatMMSS(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

fun parseTimeToMs(time: String): Long {
    val parts = time.split(":")
    if (parts.size != 2) return 0L
    val minutes = parts[0].toLongOrNull() ?: 0L
    val seconds = parts[1].toLongOrNull() ?: 0L
    return (minutes * 60 + seconds) * 1000
}

fun parseTimeToHours(time: String): Float {
    val parts = time.split(":")
    if (parts.size != 2) return 0.0f
    val minutes = parts[0].toLongOrNull() ?: 0L
    val seconds = parts[1].toLongOrNull() ?: 0L
    return (minutes * 60 + seconds) / 3600f
}


