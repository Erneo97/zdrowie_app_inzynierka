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

    fun suggestions(): List<String> {
        if (searchQuery.isBlank()) return emptyList()

        // Jeśli wpisana nazwa jest dokładnym dopasowaniem – nie pokazujemy sugestii
        val exactMatch = productList.any { it.equals(searchQuery, ignoreCase = true) }
        if (exactMatch) return emptyList()

        return productList.filter {
            it.contains(searchQuery, ignoreCase = true)
        }
    }


}