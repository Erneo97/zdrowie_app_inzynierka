package com.example.balansapp.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.components.treningplans.TrainingSeasonCard
import com.example.balansapp.ui.navigation.main.MainLayout

@Composable
fun TreningsScreen(navController: NavHostController) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.menu_trenings) ) }

    MainLayout(
        navController = navController,
        selectedItem = selectedItem,
        onItemSelected = { selectedItem = it }
    ) {
            innerPadding -> Box(

        modifier = Modifier
            .padding(innerPadding),
        contentAlignment = Alignment.Center
    ) {
        LogoBackGround()


        Column (
            modifier = Modifier.fillMaxSize().fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeadText(
                fontSize = 48.sp,
                text = "Twoje plany treningowe"
            )

            TrainingSeasonCard(
                onClick = { },
                seasonName = "Zimaq 2025",
                startDate = "05.05.25",
                endDate = "01.10.25",
                trainingCount = 55,
                isActive = true,
                goal = "Masa mięśniowa"
            )
            TrainingSeasonCard(
                onClick = { },
                seasonName = "Wiosna 2025",
                startDate = "05.05.25",
                endDate = "01.10.25",
                trainingCount = 55,
                goal = "Masa mięśniowa"
            )
            TrainingSeasonCard(
                onClick = { },
                seasonName = "Wiosna 2025",
                startDate = "05.05.25",
                endDate = "01.10.25",
                trainingCount = 55,
                goal = "Masa mięśniowa"
            )
            TrainingSeasonCard(
                onClick = { },
                seasonName = "Wiosna 2025",
                startDate = "05.05.25",
                endDate = "01.10.25",
                trainingCount = 55,
                goal = "Masa mięśniowa"
            )
            TrainingSeasonCard(
                onClick = { },
                seasonName = "Wiosna 2025",
                startDate = "05.05.25",
                endDate = "01.10.25",
                trainingCount = 55,
                goal = "Masa mięśniowa"
            )
            TrainingSeasonCard(
                onClick = { },
                seasonName = "Wiosna 2025",
                startDate = "05.05.25",
                endDate = "01.10.25",
                trainingCount = 55,
                goal = "Masa mięśniowa"
            )
            TrainingSeasonCard(
                onClick = { },
                seasonName = "Wiosna 2025",
                startDate = "05.05.25",
                endDate = "01.10.25",
                trainingCount = 55,
                goal = "Masa mięśniowa"
            )
            TrainingSeasonCard(
                onClick = { },
                seasonName = "Wiosna 2025",
                startDate = "05.05.25",
                endDate = "01.10.25",
                trainingCount = 55,
                goal = "Masa mięśniowa"
            )


        }
    }
    }



}