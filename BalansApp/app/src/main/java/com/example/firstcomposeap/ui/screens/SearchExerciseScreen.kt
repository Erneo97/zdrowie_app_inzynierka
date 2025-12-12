package com.example.firstcomposeap.ui.screens


import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import com.example.balansapp.R
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.treningplans.MuscleGroupFilter
import com.example.firstcomposeap.ui.service.SearchViewModel
import com.example.firstcomposeap.ui.service.TreningViewModel
import com.example.firstcomposeap.ui.service.data.GrupaMiesniowa
import com.example.firstcomposeap.ui.service.data.Produkt


@Composable
fun SearchExerciseScreen(
    onClose: () -> Unit,
    searchViewModel: SearchViewModel = viewModel(),
    treningViewModel: TreningViewModel,
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current
    val query = searchViewModel.searchQuery
    val suggestions = searchViewModel.suggestionsList
    val productsList = remember { mutableStateListOf<Produkt>() }

    var selectedGroups by remember { mutableStateOf(listOf<GrupaMiesniowa>()) }
    var accurately by remember { mutableStateOf(false) } // czy dokładne odwzorowanie tagów czy tylko te zawierające


    var mainText by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    BackHandler {

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
                isFocused = false
                // TODO: pobranie cwiczen
            },
        verticalArrangement = Arrangement.Top
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            NavigationButtonsRetAdd(
                onClose = {onClose() },
                onAdd = { }, // TODO: dodanie ćwiczeń
                mainText = "Dodaj Ćwiczenia do trenignu"
            )

            Text(
                "${context.getString(R.string.search)} $mainText",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(12.dp))

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
        }

        //  Lista z podpowiedziami do wyszukiwanej frazy
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


        if(!isFocused) {
            // lista produktów wyszukanych, możliwych do dodania
            LazyColumn(
                modifier = Modifier
                    .weight(9f)
                    .fillMaxWidth()
                    .heightIn(max = 650.dp)
                    .background(Color.White)
            ) {

                    items(productsList, key = { it.id }) { item ->


                        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    }


            }
        }
        else
            Text("Brak pasujących wyników wyszukiwania")

        Spacer(Modifier.height(16.dp))

    }
}




