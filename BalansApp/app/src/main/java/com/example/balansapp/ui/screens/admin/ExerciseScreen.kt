package com.example.balansapp.ui.screens.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.components.input.InputField
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayoutAdmin
import com.example.balansapp.ui.navigation.main.Screen
import com.example.balansapp.ui.service.AdminVievModel
import com.example.firstcomposeap.ui.service.TreningViewModel

@Composable
fun ExerciseAdminScreen(navController: NavHostController,
                        treningViewModel: TreningViewModel,
                        adminVievModel: AdminVievModel ) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.exercises)) }

    LaunchedEffect(Unit, adminVievModel.loadingData) {
        adminVievModel.downloadExercisesToConfirmeList()
    }

    val tabs = listOf(
        "Prośby o zaakceptowanie ćwiczenia",
        "Zarządzaj ćwiczeniami"
    )

    MainLayoutAdmin (
        navController = navController,
        selectedItem = selectedItem,
        onItemSelected = { selectedItem = it }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LogoBackGround()
            }

            Column {
                if( treningViewModel.selectedTabIndex == 0) {
                    FullSizeButton(
                        text = "Dodaj ćwiczenie",
                        onClick = { navController.navigate(Screen.NewExercise.route)},
                    )
                }
                var searchProduct by remember { mutableStateOf("") }
                if( treningViewModel.selectedTabIndex == 0) {
                    InputField(
                        value = searchProduct,
                        onValueChange = {searchProduct = it},
                        label = "Szukane ćwiczenie",
                    )
                }


                TabRow(selectedTabIndex = treningViewModel.selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = treningViewModel.selectedTabIndex == index,
                            onClick = { treningViewModel.selectedTabIndex = index },
                            text = { Text(title, fontSize = 22.sp) }
                        )
                    }
                }


                when (treningViewModel.selectedTabIndex) {
                    0 -> {
                        val filtratedProduct = adminVievModel.exercisesssList
                            .filter { it.nazwa.contains(searchProduct, ignoreCase = true)
                            }

                        LazyColumn {
                            items(filtratedProduct) {

                                Text("${it.id} - ${it.nazwa}")
                            }
                        }

                    }
                    1 -> { }
                }
            }
        }
    }
}