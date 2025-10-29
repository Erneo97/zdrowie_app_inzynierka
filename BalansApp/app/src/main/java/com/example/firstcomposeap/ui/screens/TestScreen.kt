package com.example.balansapp.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.balansapp.ui.navigation.main.BottomMenu
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.components.statistic.RadarChartElement
import androidx.compose.ui.graphics.Color
import com.example.balansapp.ui.components.statistic.RadarChartCard


@Composable
fun TestScreen() {
    var selectedItem by remember { mutableStateOf("") }

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

        val muscles = listOf(
            RadarChartElement("Biceps", 50f, Color.Red),
            RadarChartElement("Triceps", 35f, Color.Green),
            RadarChartElement("Plecy", 105f, Color.Blue),
            RadarChartElement("Klata", 70f, Color.Yellow),
            RadarChartElement("Łydki", 105f, Color.Cyan),
            RadarChartElement("Brzuch", 75f, Color.Black),
            RadarChartElement("Barki", 80f, Color.Gray),
            RadarChartElement("Kaptury", 65f, Color.Magenta)

        )
        val previous = listOf(
            RadarChartElement("Biceps", 45f, Color.Red),
            RadarChartElement("Triceps", 25f, Color.Green),
            RadarChartElement("Plecy", 75f, Color.Blue),
            RadarChartElement("Klata", 60f, Color.Yellow),
            RadarChartElement("Łydki", 105f, Color.Cyan),
            RadarChartElement("Brzuch", 75f, Color.Black),
            RadarChartElement("Barki", 85f, Color.Gray),
            RadarChartElement("Kaptury", 85f, Color.Magenta)

        )


        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            RadarChartCard(currentScope = muscles,
                previousScope = previous,
                textHead = "Trening dnia 25.11.52"
            )



        }
    }
    }


}

private fun Int.copy(alpha: Float) {}
