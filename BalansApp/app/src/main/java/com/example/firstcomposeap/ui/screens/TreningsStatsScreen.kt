package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.firstcomposeap.ui.service.TreningViewModel

@Composable
fun TreningStatsScreen(onClose: () -> Unit, treningViewModel: TreningViewModel)
{
    val statistics = treningViewModel.statisticTrening
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if( statistics!= null ) {
            Text(
                text = statistics!!.nazwa,
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