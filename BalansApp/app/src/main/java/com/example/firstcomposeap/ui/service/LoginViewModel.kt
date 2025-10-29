package com.example.balansapp.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.data.LoginRequest
import com.example.balansapp.ui.service.data.Uzytkownik
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var userId by mutableStateOf<Int?>(null)
    var user by mutableStateOf<Uzytkownik?>(null)
    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.api.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val body = response.body()
                    token = body?.token
                    userId = body?.id
                    downloadUserData()
                } else {
                    errorMessage = "Błąd logowania: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }
    fun downloadUserData() {
        viewModelScope.launch {
            try {
                val response = ApiClient.api.getUser(userId)
                if (response.isSuccessful) {
                    user = response.body()
                } else {
                    errorMessage = "Błąd logowania: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

}

