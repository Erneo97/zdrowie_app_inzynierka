package com.example.firstcomposeap.ui.service

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.ApiClient
import com.example.firstcomposeap.ui.service.data.Cwiczenie
import com.example.firstcomposeap.ui.service.data.GrupaMiesniowa
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

    fun onSearchQueryChange(value: String=searchQuery, isProduct: Boolean = true,
                            groupMuscle: List<GrupaMiesniowa> = emptyList(),
                            precision: Boolean = false
                            ) {
        searchQuery = value
        Log.e("onSearchQueryChange", "${isProduct} - ${searchQuery} = ${groupMuscle}")
        
        if( isProduct) {
            Log.e("onSearchQueryChange", "product")
            downloadSuggestionsProduct()
        }
        else {
            Log.e("onSearchQueryChange", "exercise")
            downloadSuggestionsExercise(groupMuscle, precision)
        }
    }



    fun downloadSuggestionsProduct() {
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

    private var searchedMuscleGroups = mutableStateListOf<GrupaMiesniowa>()
    private var searchedGroupPrecision = mutableStateOf(false)
    fun downloadSuggestionsExercise(groupMuscle: List<GrupaMiesniowa> = emptyList(),
                                    precision: Boolean = false
    ) {
        searchedMuscleGroups = groupMuscle.toMutableStateList()
        searchedGroupPrecision.value = precision

        viewModelScope.launch {
            try {
                Log.e("downloadSuggestionsExercise", "przewd res")
                val response = ApiClient.api.getAllMatchesExerciseNames(searchQuery,
                    grupyMiesiniowe = groupMuscle.map { it.name },
                    precision )
                Log.e("downloadSuggestionsExercise", "przewd ${response}")
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


    var searchedExercies by mutableStateOf<List<Cwiczenie>> (emptyList())
    fun downloadSearcheExercised() {
        viewModelScope.launch {
            try {
                val response = ApiClient.api.getAllExercises(searchQuery,
                    searchedMuscleGroups.toList().map { it.name },
                    searchedGroupPrecision.value
                )
                if (response.isSuccessful) {
                    val cwiczenia = response.body() ?: emptyList()
                    searchedExercies = cwiczenia
                } else {
                    errorMessage = "Błąd pobierania produktów: ${response.code()}"
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
