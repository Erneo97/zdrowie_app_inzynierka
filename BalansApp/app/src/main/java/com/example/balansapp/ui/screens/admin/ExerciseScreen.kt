package com.example.balansapp.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.components.input.InputField
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayoutAdmin
import com.example.balansapp.ui.navigation.main.Screen
import com.example.balansapp.ui.service.AdminVievModel
import com.example.firstcomposeap.ui.components.icon.Delete
import com.example.firstcomposeap.ui.components.icon.Done
import com.example.firstcomposeap.ui.components.treningplans.MuscleGroupFilter
import com.example.firstcomposeap.ui.service.SearchViewModel
import com.example.firstcomposeap.ui.service.TreningViewModel
import com.example.firstcomposeap.ui.service.data.Cwiczenie
import com.example.firstcomposeap.ui.service.data.GrupaMiesniowa

@Composable
fun ExerciseAdminScreen(navController: NavHostController,
                        treningViewModel: TreningViewModel,
                        adminVievModel: AdminVievModel ) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.exercises)) }
    val searchViewModel: SearchViewModel = viewModel()

    var selectedGroups by remember { mutableStateOf(listOf<GrupaMiesniowa>()) }
    var accurately by remember { mutableStateOf(false) } // czy dokładne odwzorowanie tagów czy tylko te zawierające

    val suggestions = searchViewModel.suggestionsList

    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val query = searchViewModel.searchQuery


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
                                ExerciseToConfirm(
                                    cwiczenie = it,
                                    onRemove = { adminVievModel.rejectExercise(it.id) },
                                    onAccept = { adminVievModel.confirmExercise( it.id) }
                                )

                            }
                        }

                    }
                    1 -> {
                        TextField(
                            value = query,
                            onValueChange = { searchViewModel.onSearchQueryChange(it, false, selectedGroups, accurately) },
                            placeholder = { Text("Nazwa ćwiczenia...") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { focusState ->
                                    isFocused = focusState.isFocused
                                }
                        )
                        AnimatedVisibility(visible = suggestions.isNotEmpty() && isFocused) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                ) {
                                    items(suggestions) { item ->
                                        Text(
                                            text = item,
                                            modifier = Modifier
                                                .clickable {
                                                    searchViewModel.selectSuggestion(item)
                                                    // pobranie cwiczen
                                                    focusManager.clearFocus()
                                                    isFocused = false
                                                }
                                                .padding(12.dp)
                                        )
                                        HorizontalDivider(
                                            Modifier,
                                            DividerDefaults.Thickness,
                                            DividerDefaults.color
                                        )
                                    }
                                }
                            }
                        }
                        MuscleGroupFilter(
                            selected = selectedGroups,
                            onSelectedChange = { groups ->
                                selectedGroups = groups
                                searchViewModel.onSearchQueryChange(isProduct =  false, groupMuscle = selectedGroups, precision =  accurately)
                                isFocused = true
                            },
                            onChangePrecision = {
                                accurately = it
                                searchViewModel.onSearchQueryChange(isProduct =  false, groupMuscle = selectedGroups, precision =  accurately)
                                isFocused = true
                            }
                        )



                        ExerciseAdminTab(  )

                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseAdminTab(  ) {

}



@Composable
fun ExerciseToConfirm(cwiczenie: Cwiczenie,
                     onRemove: () -> Unit,
                     onAccept: () -> Unit) {
    Column(modifier = getModiverCard(true)) {
        Row {
            Column (Modifier.weight(0.8f))  {
                Text("${cwiczenie.id}: ${cwiczenie.nazwa}", fontWeight = FontWeight.Bold)
                HorizontalDivider()
                Text("MET: ${cwiczenie.met}")
                Text("Opis: ${cwiczenie.opis}")
                Spacer(Modifier.height(2.dp))
                HorizontalDivider()
                Text("Dostępne grupy mięśniowe:")
                cwiczenie.grupaMiesniowas.forEach {
                    Text("\t${it.grupaNazwa}, ")

                }

            }

            Column (Modifier
                .weight(0.2f)
                .padding(6.dp) ) {
                FloatingActionButton(
                    onClick = onRemove,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(35.dp)
                ) {
                    Icon(
                        imageVector = Delete,
                        contentDescription = "Usuń element",
                        tint = Color.White
                    )
                }

                Spacer(Modifier.height(80.dp))
                FloatingActionButton(
                    onClick = onAccept,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(35.dp)
                ) {
                    Icon(
                        imageVector = Done,
                        contentDescription = "Zaakceptuj",
                        tint = Color.White
                    )
                }
            }
        }
    }
}