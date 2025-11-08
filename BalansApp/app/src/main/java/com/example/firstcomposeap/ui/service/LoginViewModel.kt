package com.example.balansapp.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.data.ChangePassword
import com.example.balansapp.ui.service.data.LoginRequest
import com.example.balansapp.ui.service.data.PommiarWagii
import com.example.balansapp.ui.service.data.Uzytkownik
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.Base64

class LoginViewModel : ViewModel() {

    var userEmail by mutableStateOf<String?>(null)
    var user by mutableStateOf<Uzytkownik?>(null)
    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)


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

    fun addWeightoUser(pommiarWagii: PommiarWagii) {
        val nowaLista = (user?.waga ?: emptyList()) + pommiarWagii
        user = user?.copy(waga = nowaLista)

        viewModelScope.launch {
            try {

                val response = ApiClient.api.addUserWeigt(pommiarWagii,"Bearer $token")
                if (response.isSuccessful) {
                    message = response.body()
                } else {
                    errorMessage = "Błąd dodania rekordu wagii: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }


    fun updateUserBasicInfo(userUpdate: Uzytkownik) {
        val basicUser = Uzytkownik(
            imie = userUpdate.imie,
            nazwisko = userUpdate.nazwisko,
            dataUrodzenia = userUpdate.dataUrodzenia,
            wzrost = userUpdate.wzrost,
            email = userUpdate.email,
            zapotrzebowanieKcal = userUpdate.zapotrzebowanieKcal,
            plec = userUpdate.plec,
            id = 0,
            aktualnyPlan = 0,
            haslo = "",
            waga = emptyList(),
            przyjaciele = emptyList(),
            dania = emptyList(),
        )

        viewModelScope.launch {
            try {
                val response = ApiClient.api.updateBasicInformationUser(basicUser,"Bearer $token")
                if (response.isSuccessful) {
                    val json = response.body()?.string()
                    try {
                        val updatedUser = Gson().fromJson(json, Uzytkownik::class.java)
                        user = updatedUser
                    } catch (e: Exception) {
                        message = json
                    }
                } else {
                    errorMessage = "Błąd dodania rekordu wagii: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

    var passwordChangeSuccess by mutableStateOf(false)
    fun updatePassword(oldPassword: String, newPassword:String ) {
        viewModelScope.launch {
            try {
                val passwordSend =
                    ChangePassword(oldPassword = oldPassword, newPassword = newPassword)
                val response = ApiClient.api.updatePasswordUser(passwordSend, "Bearer $token")
                if (response.isSuccessful) {
                    message = response.body()?.message
                    passwordChangeSuccess = true
                    errorMessage = null
                } else {
                    errorMessage = response.errorBody()?.string() ?: "Nieznany błąd (${response.code()})"
                    passwordChangeSuccess = false
                }
            }
            catch (e : Exception ) {
                errorMessage = e.localizedMessage
                passwordChangeSuccess = false
            }
        }
    }


}

