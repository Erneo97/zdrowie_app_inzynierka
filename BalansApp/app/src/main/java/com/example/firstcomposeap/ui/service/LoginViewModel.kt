package com.example.balansapp.ui.service

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.data.ChangePassword
import com.example.balansapp.ui.service.data.DaniaDetail
import com.example.balansapp.ui.service.data.LoginRequest
import com.example.balansapp.ui.service.data.Plec
import com.example.balansapp.ui.service.data.PommiarWagii
import com.example.balansapp.ui.service.data.PrzyjacieleInfo
import com.example.balansapp.ui.service.data.Uzytkownik
import com.example.balansapp.ui.service.data.ZaproszenieInfo
import com.example.firstcomposeap.ui.components.calculateUserAge
import com.example.firstcomposeap.ui.service.data.Produkt
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
    if( user == null )
        return;

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

    var isLoadedUserData by mutableStateOf(false)
    fun downloadUserData() {
        isLoadedUserData = false
        viewModelScope.launch {
            try {

                val response = ApiClient.api.getUser("Bearer $token")
                if (response.isSuccessful) {
                    user = response.body()
                    isLoadedUserData = true
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
                    message = response.body()?.message
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


    var isListFrendsLoaded by mutableStateOf(false)
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

    suspend  fun downloadFrendsListICanModife() : List<PrzyjacieleInfo> {
        isListFrendsLoaded = false
        return try {
            val response = ApiClient.api.getUserFrendsICanModife("Bearer $token")
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            errorMessage = e.localizedMessage
            emptyList()
        }
        finally {
            isListFrendsLoaded = true
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

    fun changeAccessUserFrend(przyjaciel: PrzyjacieleInfo ) {
        invitationResult = null
        viewModelScope.launch {
            try {
                val response = ApiClient.api.changeAccessUserFrend(przyjaciel, "Bearer $token")
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


    fun addNewRecipe(produkty: List<Produkt>, nazwa : String ) {
        Log.e("updateUserRecipe", " $nazwa")
        val new  = DaniaDetail(
            id = -1,
            nazwa = nazwa,
            listaProdukty = produkty
        )

        userRecipesList.add(new)
        updateRecipesUser()

    }


    fun updateRecipesUser( ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.api.updateUserRecipes(userRecipesList, "Bearer $token")
                if( response.isSuccessful) {
                    invitationResult = InvitationResult.Success( "Zaktualizowane przepisy użytkownika")
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


    fun downloadUserRecipes( ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.api.downloadUserRecipes("Bearer $token")
                if( response.isSuccessful) {
                    userRecipesList.clear()
                    userRecipesList.addAll(response.body()!!)
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

    var userRecipesList = mutableStateListOf<DaniaDetail>() // lista przepisów danego użytkownika

//    fun initUserRecipesList() {
//        userRecipesList.addAll(listOf(DaniaDetail(
//            id = 1,
//            nazwa = "Sałatka grecka",
//            listaProdukty = listOf(
//                Produkt(
//                    id = 101,
//                    nazwa = "Ser feta",
//                    producent = "Mlekpol",
//                    kodKreskowy = "5901234567890",
//                    objetosc = listOf(
//                        Dawka(
//                            jednostki = Jednostki.GRAM,
//                            wartosc = 50f,
//                            kcal = 132f,
//                            bialko = 7f,
//                            weglowodany = 1f,
//                            tluszcze = 11f,
//                            blonnik = 0f
//                        )
//                    )
//                ),
//                Produkt(
//                    id = 102,
//                    nazwa = "Pomidor",
//                    producent = "Rolnik",
//                    kodKreskowy = "5900987654321",
//                    objetosc = listOf(
//                        Dawka(
//                            jednostki = Jednostki.GRAM,
//                            wartosc = 100f,
//                            kcal = 18f,
//                            bialko = 1f,
//                            weglowodany = 4f,
//                            tluszcze = 0f,
//                            blonnik = 1f
//                        )
//                    )
//                )
//            )
//        ),
//            DaniaDetail(
//                id = 2,
//                nazwa = "Kurczak z ryżem",
//                listaProdukty = listOf(
//                    Produkt(
//                        id = 201,
//                        nazwa = "Pierś z kurczaka",
//                        producent = "Drobex",
//                        kodKreskowy = "5901111111111",
//                        objetosc = listOf(
//                            Dawka(
//                                jednostki = Jednostki.GRAM,
//                                wartosc = 150f,
//                                kcal = 165f,
//                                bialko = 31f,
//                                weglowodany = 0f,
//                                tluszcze = 3.6f,
//                                blonnik = 0f
//                            )
//                        )
//                    ),
//                    Produkt(
//                        id = 202,
//                        nazwa = "Ryż biały",
//                        producent = "Kupiec",
//                        kodKreskowy = "5902222222222",
//                        objetosc = listOf(
//                            Dawka(
//                                jednostki = Jednostki.GRAM,
//                                wartosc = 180f,
//                                kcal = 230f,
//                                bialko = 4f,
//                                weglowodany = 50f,
//                                tluszcze = 1f,
//                                blonnik = 1f
//                            )
//                        )
//                    )
//                )
//            ),
//            DaniaDetail(
//                id = 4,
//                nazwa = "Kanapka z szynką",
//                listaProdukty = listOf(
//                    Produkt(
//                        id = 401,
//                        nazwa = "Chleb żytni",
//                        producent = "Bielmar",
//                        kodKreskowy = "5905555555555",
//                        objetosc = listOf(
//                            Dawka(
//                                jednostki = Jednostki.GRAM,
//                                wartosc = 70f,
//                                kcal = 165f,
//                                bialko = 5f,
//                                weglowodany = 30f,
//                                tluszcze = 1.5f,
//                                blonnik = 4f
//                            )
//                        )
//                    ),
//                    Produkt(
//                        id = 402,
//                        nazwa = "Szynka gotowana",
//                        producent = "Sokołów",
//                        kodKreskowy = "5906666666666",
//                        objetosc = listOf(
//                            Dawka(
//                                jednostki = Jednostki.GRAM,
//                                wartosc = 30f,
//                                kcal = 39f,
//                                bialko = 6f,
//                                weglowodany = 1f,
//                                tluszcze = 1f,
//                                blonnik = 0f
//                            )
//                        )
//                    )
//                )
//            )
//
//        )
//        )
//
//
//
//    }

}

