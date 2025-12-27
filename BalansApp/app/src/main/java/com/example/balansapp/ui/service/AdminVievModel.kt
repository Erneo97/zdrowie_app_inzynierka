package com.example.balansapp.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.data.UserCard
import kotlinx.coroutines.launch

class AdminVievModel : ViewModel() {
    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)
    var token by mutableStateOf<String?>(null)

    var usersList = mutableListOf<UserCard>()


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

}