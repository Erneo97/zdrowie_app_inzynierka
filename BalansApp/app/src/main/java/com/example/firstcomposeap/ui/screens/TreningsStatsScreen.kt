package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import com.example.balansapp.ui.components.statistic.RadarChart
import com.example.balansapp.ui.components.statistic.RadarChartElement
import com.example.firstcomposeap.ui.components.icon.Arrow_back_ios_new
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
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Aktualny: ${statistics.dateCurrent}")
            Text(text = "Poprzedni: ${statistics.datePrevious}")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Spalone kalorie: %.2f".format(statistics.spaloneKalorie),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Ćwiczenia:", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier.heightIn(max = 200.dp)
            ) {
                items(statistics.trening) { cwiczenie ->
                    Text(text = "- ${cwiczenie.nazwa}")
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Obciążenie wg grup mięśniowych (aktualny):")
                    statistics.current.forEach { (grupa, wartosc) ->
                        Text(text = "${grupa.grupaNazwa}: %.2f".format(wartosc))
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Obciążenie wg grup mięśniowych (poprzedni):")
                    statistics.previous.forEach { (grupa, wartosc) ->
                        Text(text = "${grupa.grupaNazwa}: %.2f".format(wartosc))
                    }
                }
            }
        }
    }

}