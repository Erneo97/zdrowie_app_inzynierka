package com.example.balansapp.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.data.RegisterRequest
import com.example.balansapp.ui.service.data.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {

    var registerState by mutableStateOf<RegisterResponse?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    fun registerUser(name: String, surname: String, email: String, password: String, plec: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.api.register(
                    RegisterRequest(name, surname, email, password, plec)
                )
                if (response.isSuccessful) {
                    registerState = response.body()
                } else {
                    errorMessage = "Błąd: ${response.code()} - ${response.errorBody()?.string() }"
                }
            } catch (e: HttpException) {
                errorMessage = "Błąd HTTP: ${e.message}"
            } catch (e: Exception) {
                errorMessage = "Nieoczekiwany błąd: ${e.localizedMessage}"
            }
        }
    }
}
