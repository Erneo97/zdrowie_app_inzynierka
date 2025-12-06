package com.example.balansapp.ui.screens


import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.balansapp.ui.navigation.main.BottomMenu
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.components.statistic.RadarChartElement
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.balansapp.ui.components.statistic.RadarChartCard
import kotlinx.coroutines.delay


@Composable
fun TestScreen() {
    var selectedItem by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var elapsedMs by remember { mutableStateOf(0L) }


    LaunchedEffect(isRunning) {
        if (isRunning) {
            startTime = SystemClock.uptimeMillis()
            Log.e("czas", "${formatMs(elapsedMs)} - ${elapsedMs}")
            while (isRunning) {
                elapsedMs = SystemClock.uptimeMillis() - startTime
                delay(100)
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomMenu(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) {
        innerPadding -> Box(

        modifier = Modifier
            .padding(innerPadding).fillMaxSize().verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        LogoBackGround()

            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = formatMs(elapsedMs))

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(onClick = { isRunning = true }) {
                        Text("Start")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { isRunning = false }) {
                        Text("Stop")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        isRunning = false
                        elapsedMs = 0L
                        startTime = 0L
                    }) {
                        Text("Reset")
                    }
                }


            }
        }
    }

}


fun formatMs(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val hundredths = (ms % 1000) / 10
    return "%02d:%02d.%02d".format(minutes, seconds, hundredths)
}

