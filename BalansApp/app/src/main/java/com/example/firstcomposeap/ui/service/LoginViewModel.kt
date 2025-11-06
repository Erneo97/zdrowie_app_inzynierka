package com.example.balansapp.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.data.LoginRequest
import com.example.balansapp.ui.service.data.Uzytkownik
import kotlinx.coroutines.launch
import java.util.Base64

class LoginViewModel : ViewModel() {
    var userEmail by mutableStateOf<String?>(null)
    var user by mutableStateOf<Uzytkownik?>(null)
    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val credentials = "$email:$password"
                val basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.toByteArray())
                val response = ApiClient.api.login(
                    LoginRequest(email, password),
                    basicAuth
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    token = body?.token
                    userEmail = body?.email
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

                val response = ApiClient.api.getUser("Bearer $token")
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

