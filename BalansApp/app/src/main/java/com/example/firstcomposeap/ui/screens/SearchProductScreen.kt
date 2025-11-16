package com.example.firstcomposeap.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.balansapp.R
import com.example.firstcomposeap.ui.components.meal.friendsMealTab
import com.example.firstcomposeap.ui.components.meal.userMealTab
import com.example.firstcomposeap.ui.service.SearchViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun SearchProductScreen(
    onClose: () -> Unit,
    searchViewModel: SearchViewModel = viewModel()
) {
    val tabs = listOf("Produkty",
        "Danie"
    )
    var mainText by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> searchViewModel.setSearcProducts()
            1 -> searchViewModel.setSearcMeal()
        }
        when (selectedTabIndex) {
            0 -> mainText = "Wyszukaj produkt"
            1 -> mainText = "Wyszukaj danie"
        }
    }


    searchViewModel.productList = listOf("Masło", "Masło 1", "Masło 2 ", "Masło 33", "Mleko", "Mąka", "Cukier", "Chleb")
    searchViewModel.mealList = listOf("Sphagettin", "Mielony", "Schabowy", "Galaretka", "Budyń")

    val query = searchViewModel.searchQuery
    val suggestions = searchViewModel.suggestions()

    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxSize()
            .padding(16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
                isFocused = false
            }
    ) {


        Text(mainText, style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(12.dp))

        TextField(
            value = query,
            onValueChange = { searchViewModel.searchQuery = it },
            placeholder = { Text("Nazwa produktu...") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
        )

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTabIndex == index,
                    onClick = {selectedTabIndex = index},
                    text =  {Text(title, fontSize = 22.sp)}
                )

            }
        }
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
                                    searchViewModel.searchQuery = item
                                }
                                .padding(12.dp)
                        )
                        Divider()
                    }
                }
            }
        }

        // lista znalezionych produktó
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            items(suggestions) { item ->
                SearchedItem(
                    nazwa = item,
                )
                Divider()
            }
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onClose( )
                      searchViewModel.searchQuery = ""
                      },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Anuluj")
        }
    }
}


@Composable
fun SearchedItem( nazwa: String) {
    var shadowColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .drawBehind {
                val shadowOffsetX = 8f
                val shadowOffsetY = 8f
                val shadowColor = shadowColor.copy(alpha = 0.25f)
                drawRoundRect(
                    color = shadowColor,
                    topLeft = Offset(shadowOffsetX, shadowOffsetY),
                    size = size,
                    cornerRadius = CornerRadius(25f, 25f),
                )
            }
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(25.dp),
                ambientColor = shadowColor.copy(alpha = 0.8f),
                spotColor = shadowColor.copy(alpha = 0.8f)
            )
            .border(
                width = 2.dp,
                color = Color.Gray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(25.dp)
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = nazwa,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                FloatingActionButton(
                    onClick = {},
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

