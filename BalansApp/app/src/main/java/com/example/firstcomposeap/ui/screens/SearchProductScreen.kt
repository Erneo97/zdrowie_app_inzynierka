package com.example.firstcomposeap.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firstcomposeap.ui.service.SearchViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun SearchProductScreen(
    onClose: () -> Unit,
    searchViewModel: SearchViewModel = viewModel()
) {



    searchViewModel.productList = listOf("Masło", "Mleko", "Mąka", "Cukier", "Chleb")

    val query = searchViewModel.searchQuery
    val suggestions = searchViewModel.suggestions()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text("Wyszukaj produkt", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(12.dp))

        TextField(
            value = query,
            onValueChange = { searchViewModel.searchQuery = it },
            placeholder = { Text("Nazwa produktu...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedVisibility(visible = suggestions.isNotEmpty()) {
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

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onClose( )
                      searchViewModel.searchQuery = ""
                      },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Zamknij")
        }
    }
}
