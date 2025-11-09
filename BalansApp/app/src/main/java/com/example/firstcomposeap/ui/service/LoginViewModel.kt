package com.example.balansapp.ui.service

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.data.ChangePassword
import com.example.balansapp.ui.service.data.LoginRequest
import com.example.balansapp.ui.service.data.Plec
import com.example.balansapp.ui.service.data.PommiarWagii
import com.example.balansapp.ui.service.data.PrzyjacieleInfo
import com.example.balansapp.ui.service.data.Uzytkownik
import com.example.balansapp.ui.service.data.ZaproszenieInfo
import com.example.firstcomposeap.ui.components.calculateUserAge
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.Base64

class LoginViewModel : ViewModel() {

    var userEmail by mutableStateOf<String?>(null)
    var user by mutableStateOf<Uzytkownik?>(null)
    var ppm by mutableStateOf<Double>(0.0)

    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)

//     Podstawowa Przemiana Materii (PPM) wzór Mifflina
@SuppressLint("SuspiciousIndentation")
fun calculatePPM( ) {
        val wagaUzytkownika : Double = user!!.waga.last().wartosc
        val dataUrodzenia : String = user!!.dataUrodzenia

        ppm = 10.0 * wagaUzytkownika + (6.25 * user!!.wzrost)
                - (5.0 * calculateUserAge(dataUrodzenia))
        if( user!!.plec == Plec.KOBIETA) {
            ppm = ppm - 161
        }
        else {
            ppm = ppm  - 5
        }
    }

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
                    calculatePPM()
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




//  -----------------   ZNAJOMI -------------------------------
    sealed class InvitationResult {
        data class Success(val message: String) : InvitationResult()
        data class Error(val message: String) : InvitationResult()
    }


    var invitationResult by mutableStateOf<InvitationResult?>(null)
    fun invitateUser(email: String ) {
        invitationResult = null
        viewModelScope.launch {
            try {
                val response = ApiClient.api.inviteFriend(email, "Bearer $token")
                if( response.isSuccessful) {
                    Log.d("Invite", "isSuccessful: ${response.isSuccessful}, body: ${response.body()?.message}, code: ${response.code()}")
                    invitationResult = InvitationResult.Success( "Wysłano zaproszenie")
                }
                else {
                    invitationResult = InvitationResult.Error(response.errorBody()?.string() ?: "Nieznany błąd")
                }
            }
            catch (e : Exception) {
                errorMessage = e.localizedMessage
            }
        }

    }

    suspend  fun downloadInfitationInformationList() : List<ZaproszenieInfo> {
        return try {
            val response = ApiClient.api.getAllInvitationUser("Bearer $token")
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            errorMessage = e.localizedMessage
            emptyList()
        }
    }


    fun acceptInvitation(zaproszenie: ZaproszenieInfo ) {
        invitationResult = null
        viewModelScope.launch {
            try {
                val response = ApiClient.api.akceptInvitation(zaproszenie, "Bearer $token")
                if( response.isSuccessful) {
                    Log.d("Invite", "isSuccessful: ${response.isSuccessful}, body: ${response.body()?.message}, code: ${response.code()}")
                    invitationResult = InvitationResult.Success( "Zaakceptowano zaproszenie")
                }
                else {
                    invitationResult = InvitationResult.Error(response.errorBody()?.string() ?: "Nieznany błąd")
                }
            }
            catch (e : Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }


    fun cancelInvitation(zaproszenie: ZaproszenieInfo ) {
        invitationResult = null
        viewModelScope.launch {
            try {
                val response = ApiClient.api.cancelInvitation(zaproszenie, "Bearer $token")
                if( response.isSuccessful) {
                    Log.d("Invite", "isSuccessful: ${response.isSuccessful}, body: ${response.body()?.message}, code: ${response.code()}")
                    invitationResult = InvitationResult.Success( "Zaakceptowano zaproszenie")
                }
                else {
                    invitationResult = InvitationResult.Error(response.errorBody()?.string() ?: "Nieznany błąd")
                }
            }
            catch (e : Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

    suspend  fun downloadFrendsInformationList() : List<PrzyjacieleInfo> {
        return try {
            val response = ApiClient.api.getUserFrends("Bearer $token")
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            errorMessage = e.localizedMessage
            emptyList()
        }
    }


    fun deleteUserFrend(przyjaciel: PrzyjacieleInfo ) {
        invitationResult = null
        viewModelScope.launch {
            try {
                val response = ApiClient.api.deleteUserFrend(przyjaciel, "Bearer $token")
                if( response.isSuccessful) {
                    invitationResult = InvitationResult.Success( "Usunięto przyjaciela")
                }
                else {
                    invitationResult = InvitationResult.Error(response.errorBody()?.string() ?: "Nieznany błąd")
                }
            }
            catch (e : Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }
}

