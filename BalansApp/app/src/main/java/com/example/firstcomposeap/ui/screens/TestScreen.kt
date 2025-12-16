package com.example.balansapp.ui.screens


import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.balansapp.ui.navigation.main.BottomMenu
import com.example.balansapp.ui.components.input.LogoBackGround

import androidx.compose.ui.unit.dp
import com.example.firstcomposeap.ui.components.statistic.LineChartWithControls
import com.example.firstcomposeap.ui.service.StatisticViewModel
import com.example.firstcomposeap.ui.service.data.ChartPoint
import kotlinx.coroutines.delay


@Composable
fun TestScreen(statisticViewModel: StatisticViewModel) {
    var selectedItem by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var elapsedMs by remember { mutableStateOf(0L) }


    LaunchedEffect(Unit, statisticViewModel.token) {
        statisticViewModel.downloadWeightsUserStatistic()
    }

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
                Column(modifier = Modifier.padding(16.dp)) {
                    statisticViewModel.weightStats.forEach { stats ->
                        stats?.let {
                            Text(text = "Median: ${it.median}")
                            Text(text = "Min: ${it.min}")
                            Text(text = "Max: ${it.max}")
                            Text(text = "Trend: y = ${it.a}x + ${it.b}")
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }


                Spacer(Modifier.height(15.dp))
                LineChartWithControls(
                    points = listOf(
                        ChartPoint(0.0, 10.0),
                        ChartPoint(1.0, 15.0),
                        ChartPoint(2.0, 12.0),
                        ChartPoint(3.0, 20.0)
                    ),
                    xAxisLabel = "Dni",
                    yAxisLabel = "Wartość",
                    modifier = Modifier.padding(16.dp),
                    a = 2.2,
                    b = 11.0
                )


            }
        }
    }

}


private fun formatMs(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val hundredths = (ms % 1000) / 10
    return "%02d:%02d.%02d".format(minutes, seconds, hundredths)
}



