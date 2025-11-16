package com.example.firstcomposeap.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SearchViewModel @Inject constructor(): ViewModel() {
    var searchQuery by mutableStateOf("")
    var token by mutableStateOf<String?>(null)

    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)

    var productList by mutableStateOf(listOf<String>())
    var mealList by mutableStateOf(listOf<String>())
    var searchItemsList by mutableStateOf(mealList)

    fun setSearcMeal( ) {
        searchQuery = ""
        searchItemsList = mealList
    }

    fun setSearcProducts( ) {
        searchQuery = ""
        searchItemsList = productList
    }

    fun suggestions(): List<String> {
        if (searchQuery.isBlank()) return emptyList()

        // Jeśli wpisana nazwa jest dokładnym dopasowaniem – nie pokazujemy sugestii
        val exactMatch = searchItemsList.any { it.equals(searchQuery, ignoreCase = true) }
        if (exactMatch) return emptyList()

        return searchItemsList.filter {
            it.contains(searchQuery, ignoreCase = true)
        }
    }


}