package com.example.balansapp.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.components.treningplans.TrainingSeasonCard
import com.example.balansapp.ui.navigation.main.MainLayout
import com.example.balansapp.ui.navigation.main.Screen
import com.example.firstcomposeap.ui.service.TreningViewModel


@Composable
fun TreningsPlanScreen(navController: NavHostController, treningViewModel: TreningViewModel)
{
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.menu_plans)) }

    LaunchedEffect(Unit) {
        treningViewModel.getUserTreningPlansCard()
    }

    MainLayout(
        navController = navController,
        selectedItem = selectedItem,
        onItemSelected = { selectedItem = it }
    ) {
            innerPadding -> Box(
        modifier = Modifier
            .padding(innerPadding),
        contentAlignment = Alignment.Center  )
        {
            LogoBackGround()
            Column {
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { navController.navigate(Screen.NewExercise.route) },// TODO: Dodaj ćwiczenie
                        modifier = Modifier.weight(2f)
                    ) {
                        Text("Dodaj ćwiczenie", fontSize = 15.sp)
                    }

                    Button(
                        onClick = { navController.navigate(Screen.NewTreningPlan.route) }, // TODO: Nowy plan treningowy
                        modifier = Modifier.weight(3f)
                    ) {
                        Text("Nowy plan treningowy", fontSize = 15.sp)
                    }
                }

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    treningViewModel.treningsPlanCard.forEach { item ->
                        TrainingSeasonCard(
                            onClick = { },
                            treningPlan = item
                        )
                    }
                }
            }
        }
    }
}
