package com.example.balansapp.ui.service

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.data.UserCard
import com.example.firstcomposeap.ui.service.data.Cwiczenie
import com.example.firstcomposeap.ui.service.data.Produkt
import kotlinx.coroutines.launch

class AdminVievModel : ViewModel() {
    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)
    var token by mutableStateOf<String?>(null)

    var usersList = mutableListOf<UserCard>()



    var productssList = mutableStateListOf<Produkt>()
    fun confirmProduct(id :Int ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.getApi(token ?: "").acceptProduct(id)
                if (response.isSuccessful) {
                    productssList.removeIf { it.id == id.toLong() }
                } else {
                    errorMessage = "Błąd zaakceptowania produktu: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

    fun rejectProduct(id :Int ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.getApi(token ?: "").rejectProduct(id)
                if (response.isSuccessful) {
                    Log.e("rejectProduct", "isSuccessful")
                    productssList.removeIf { it.id == id.toLong() }
                } else {
                    Log.e("rejectProduct", "error ${response.errorBody()}")
                    errorMessage = "Błąd odrzucenia produktu: ${response.errorBody()}"
                }
            } catch (e: Exception) {
                Log.e("rejectProduct", "catch ${e.localizedMessage}")
                errorMessage = e.localizedMessage
            }
            finally {
                loadingData = false
            }
        }
    }
    fun downloadProducToConfirmeList( ) {
        loadingData = true
        viewModelScope.launch {
            try {

                val response = ApiClient.getApi(token ?: "").getProductListToCheck()
                if (response.isSuccessful) {
                    response.body()?.let {
                        productssList.clear()
                        productssList.addAll(it)
                    }
                } else {
                    errorMessage = "Błąd pobierania listy produktów: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
            finally {
                loadingData = false
            }
        }
    }

    var loadingData by mutableStateOf(false)


    fun downloadUserList( ) {
        loadingData = true
        viewModelScope.launch {
            try {

                val response = ApiClient.getApi(token ?: "").getUsersCards()
                if (response.isSuccessful) {
                    response.body()?.let {
                        usersList.clear()
                        usersList.addAll(it)
                    }
                } else {
                    errorMessage = "Błąd pobierania listy użytkowników: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
            finally {
                loadingData = false
            }
        }
    }

    fun changeStatsUser(id: Int, block: Boolean) {
        viewModelScope.launch {
            try {

                val response = ApiClient.getApi(token ?: "").upadateStatusUser(id = id, status = block)
                if (response.isSuccessful) {
                    response.body()?.let {
                        message = it.message
                    }
                } else {
                    errorMessage = "Błąd zmiany statusu użytkownika: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }


    var exercisesssList = mutableStateListOf<Cwiczenie>()

    fun downloadExercisesToConfirmeList( ) {
        loadingData = true
        viewModelScope.launch {
            try {

                val response = ApiClient.getApi(token ?: "").getExercisesToCheck()
                if (response.isSuccessful) {
                    response.body()?.let {
                        exercisesssList.clear()
                        exercisesssList.addAll(it)
                    }
                } else {
                    errorMessage = "Błąd pobierania listy produktów: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
            finally {
                loadingData = false
            }
        }
    }


}