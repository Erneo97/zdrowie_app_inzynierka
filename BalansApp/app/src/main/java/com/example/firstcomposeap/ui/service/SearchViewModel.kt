package com.example.firstcomposeap.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.ApiClient
import com.example.firstcomposeap.ui.service.data.Produkt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Lista sugestii z backendu
    var suggestionsList by mutableStateOf<List<String>>(emptyList())
        private set

    // Finalne wybrane produkty lub posiłki
    var resultsList by mutableStateOf<List<String>>(emptyList())
        private set

    var searchedProducts by mutableStateOf<List<Produkt>> (emptyList())

    fun onSearchQueryChange(value: String, isProduct: Boolean = true) {
        searchQuery = value

        if (value.isBlank()) {
            suggestionsList = emptyList()
            return
        }
        
        if( isProduct) {
            downloadSuggestionsProduct()
        }
        else {
            downloadSuggestionsExercise()
        }
    }


    fun downloadSuggestionsProduct() {
//        Log.e("downloadSuggestionsProduct", " ${searchQuery}")
        viewModelScope.launch {
            try {
                val response = ApiClient.api.getAllMatchesProduktNames(searchQuery)
                if (response.isSuccessful) {
                    suggestionsList = response.body() ?: emptyList()
                } else {
                    errorMessage = "Błąd pobierania sugestii: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

    fun downloadSuggestionsExercise() {
//        Log.e("downloadSuggestionsExercise", " ${searchQuery}")
        viewModelScope.launch {
            try {
                val response = ApiClient.api.getAllMatchesExerciseNames(searchQuery)
                if (response.isSuccessful) {
                    suggestionsList = response.body() ?: emptyList()
                } else {
                    errorMessage = "Błąd pobierania sugestii: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

    fun downloadSearcheProducts() {
        viewModelScope.launch {
            try {
                val response = ApiClient.api.getAllMatchesProduct(searchQuery)
                if (response.isSuccessful) {
                    val produkty = response.body() ?: emptyList()
                    searchedProducts = produkty
                } else {
                    errorMessage = "Błąd pobierania produktów: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }


    fun selectSuggestion(suggestion: String) {
        searchQuery = suggestion
        suggestionsList = emptyList()
        resultsList = listOf(suggestion)
    }




}
