package com.example.balansapp.ui.screens


import android.annotation.SuppressLint
import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.balansapp.ui.navigation.main.BottomMenu
import com.example.balansapp.ui.components.input.LogoBackGround

import androidx.compose.ui.unit.dp
import com.example.firstcomposeap.ui.components.meal.SelectBox
import com.example.firstcomposeap.ui.components.meal.SelectPomiarWagiOptionBox
import com.example.firstcomposeap.ui.components.statistic.LineChartWithControls
import com.example.firstcomposeap.ui.service.StatisticViewModel
import com.example.firstcomposeap.ui.service.data.ChartPoint
import com.example.firstcomposeap.ui.service.data.PomiarWagiOptions
import com.example.firstcomposeap.ui.service.data.StatisticParameters
import com.example.firstcomposeap.ui.service.data.StatisticPeriod
import kotlinx.coroutines.delay
import okhttp3.internal.format
import java.time.LocalDate


@Composable
fun TestScreen(statisticViewModel: StatisticViewModel) {
    var selectedItem by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var elapsedMs by remember { mutableStateOf(0L) }

    var selectedPeriod by remember { mutableStateOf(StatisticPeriod.WEEK) }
    var days by remember { mutableStateOf(StatisticPeriod.WEEK.days) }
    var localDate by remember {  mutableStateOf<LocalDate>(LocalDate.now()) }

    var selectOption by remember {  mutableStateOf(PomiarWagiOptions.WAGA) }
    var selectedValue by remember {  mutableStateOf<StatisticParameters?>(null) }
    var selectedLabel by remember {  mutableStateOf<String?>(null) }
    var points = remember { mutableStateListOf<ChartPoint>() }


    LaunchedEffect(selectOption, days, localDate) {
        selectedLabel = selectOption.label
        selectedValue = statisticViewModel.getCorrectStatisticParameters(selectOption)
    }

    LaunchedEffect(Unit, statisticViewModel.token, days, localDate) {
        statisticViewModel.downloadWeightsUserStatistic(days, localDate)
        statisticViewModel.downloadWeightsDataUser(days, localDate)

        points.apply {
            points.clear()
            points.addAll(statisticViewModel.getDataByOption(selectOption))
        }
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
        innerPadding -> Box {
        LogoBackGround()



            Column (modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),

                ) {
                SelectPomiarWagiOptionBox(
                    options = PomiarWagiOptions.entries,
                    selectedOption = selectOption,
                    onOptionSelected = {selectOption = it}
                )

                StatisticPeriodSelector(
                    selectedPeriod = selectedPeriod,
                    onSelected = {
                        Log.e("downloadWeightsUserStatistic", "czas wybrany: ${it} - ${selectedPeriod}")
                        selectedPeriod = it
                        days = it.days
                    }
                )



                selectedValue?.let { value ->
                    StatisticsCard(
                        stats = value,
                        label =  selectedLabel ?: "Statystyki"
                    )
                    Spacer(Modifier.height(15.dp))

                    points.let {
                        LineChartWithControls(
                            points = it.toList(),
                            xAxisLabel = "Dni",
                            yAxisLabel = "Wartość",
                            modifier = Modifier.padding(16.dp),
                            a = value.a,
                            b = value.b
                        )
                    }
                }





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



@Composable
fun StatisticsCard(
    stats: StatisticParameters?,
    modifier: Modifier = Modifier,
    label: String = "Statystyki"
) {

    Card(
        modifier = modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge
            )


            Spacer(modifier = Modifier.height(16.dp))

            stats?.let {
                StatisticRow("Min", it.min)
                StatisticRow("Max", it.max)
                StatisticRow("Średnia", it.average)
                StatisticRow("Mediana", it.median)
            } ?: run {
                Text("Brak danych", color = Color.Gray)
            }
        }
    }
}

@Composable
fun StatisticPeriodSelector(
    selectedPeriod: StatisticPeriod,
    onSelected: (StatisticPeriod) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()
            ,horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            StatisticPeriod.entries.forEach { period ->
                FilterChip(
                    selected = period == selectedPeriod,
                    onClick = { onSelected(period) },
                    label = { Text(period.label) },
                    modifier = Modifier.weight(2f)
                    
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun StatisticRow(label: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(
            text = String.format("%.2f", value),
            fontWeight = FontWeight.Bold
        )
    }
}
