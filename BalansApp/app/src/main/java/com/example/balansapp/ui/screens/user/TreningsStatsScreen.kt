package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.statistic.RadarChart
import com.example.balansapp.ui.components.statistic.RadarChartElement
import com.example.firstcomposeap.ui.components.icon.Arrow_back_ios_new
import com.example.firstcomposeap.ui.components.treningplans.CwiczenieTreningStatsItem
import com.example.firstcomposeap.ui.service.TreningViewModel

@Composable
fun TreningStatsScreen(onClose: () -> Unit, treningViewModel: TreningViewModel)
{
    val statistics = treningViewModel.statisticTrening

    val currentScope by remember(statistics) {
        derivedStateOf {
            statistics?.current
                ?.map { (g, v) -> RadarChartElement(g.grupaNazwa, v,  Color.Green) }
                ?: emptyList()
        }
    }

    val previousScope by remember(statistics) {
        derivedStateOf {
            statistics?.previous
                ?.map { (g, v) -> RadarChartElement(g.grupaNazwa, v, Color.Red) }
                ?: null
        }
    }

    val legendText by remember(statistics) {
        derivedStateOf {
            statistics?.let {
                listOf(it.dateCurrent, it.datePrevious)
            } ?: emptyList()
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        FloatingActionButton(
            onClick = onClose,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Arrow_back_ios_new,
                contentDescription = "Anuluj",
                tint = Color.White
            )
        }
        Spacer(Modifier.height(15.dp))

        if( statistics!= null ) {
            RadarChart(
                currentScope =currentScope,
                previousScope= previousScope,
                legentText = legendText
            )

            Text(
                text = statistics.nazwa,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 29.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Data treningu: ${statistics.dateCurrent}"
                , fontWeight = FontWeight.Bold
            )
            if( previousScope != null )
                Text(text = "Data treningu poprzedniego: ${statistics.datePrevious}")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Spalone kalorie: %.2f kcal".format(statistics.spaloneKalorie),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Wykonane ćwiczenia:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 23.sp
            )

            statistics.trening.forEach { cwiczenie ->
                CwiczenieTreningStatsItem(
                    cwiczenie = cwiczenie
                )
            }
            Spacer(Modifier.height(10.dp))

        }
    }
}