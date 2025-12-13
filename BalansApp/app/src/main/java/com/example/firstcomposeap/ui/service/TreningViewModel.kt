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
import com.example.firstcomposeap.ui.service.data.GOAL
import com.example.firstcomposeap.ui.service.data.GrupaMiesniowa
import com.example.firstcomposeap.ui.service.data.PlanTreningowy
import com.example.firstcomposeap.ui.service.data.cwiczeniaPlanuTreningowego
import com.example.firstcomposeap.ui.service.data.treningsPlanCard
import kotlinx.coroutines.launch


class TreningViewModel : ViewModel() {
    var nazwa by   mutableStateOf("")
    var cel by  mutableStateOf("Wybierz cel")
    var aktualny by mutableStateOf(false)
    var idPlanu by mutableStateOf(-1)


    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)

    val selectedExercised = mutableStateListOf<Cwiczenie>()
    val selectedExercisedOnNewTP = mutableStateListOf<cwiczeniaPlanuTreningowego>()

    fun validateCwiczeniaWPlanie( ) : Boolean {
        selectedExercisedOnNewTP.forEach { it ->
            if( it.serie.isEmpty() )
                return false
        }

        return true
    }

//    var currentFunction: () -> Unit
    var buttonText by mutableStateOf("")
    var newOrEdit by mutableStateOf(false)
    fun setTreningPlanScreen(newOrEdit: Boolean = false ) {
        this.newOrEdit = newOrEdit
        if( newOrEdit) {
            buttonText = "Aktualizuj plan Treningowy"
        }
        else {
            buttonText = "Zapisz plan Treningowy"
        }
    }

    fun init(nazwa: String = "", cel: String ="Wybierz cel", aktualny: Boolean = false, id : Int = -1 ) {
        this.nazwa = nazwa
        this.cel = cel
        this.aktualny = aktualny
        this.idPlanu = id

        if( this.newOrEdit ) {
            downloadTreningDetail()
//          TODO:  pobieranie cwiczen wewnątrz planu treningowego
        }

    }

    fun aktualizujBazeDanych(nazwa: String ="", aktualny: Boolean = true, cel : GOAL = GOAL.CONST ) {
        if( newOrEdit) {
            updateTreningPlan()
        }
        else {
            createNewTreningPlan(nazwa = nazwa, aktualny = aktualny, cel = cel) // tworzy plan treningowy gdy ktos wybran "Nowy plan treningowy"
        }
    }

    fun addNewExerciseToPlan(cwiczenie: Cwiczenie ) {
        selectedExercised.add(cwiczenie)
        selectedExercisedOnNewTP.add(cwiczeniaPlanuTreningowego(
            id = cwiczenie.id,
            nazwa = cwiczenie.nazwa,
            grupaMiesniowas = cwiczenie.grupaMiesniowas,
        ))
    }
    fun removeExerciseFromPlan(cwiczenie: Cwiczenie ) {
        selectedExercised.remove(cwiczenie)
        val opt = selectedExercisedOnNewTP.find { it.id == cwiczenie.id }
        if (opt != null) {
            selectedExercisedOnNewTP.remove(opt)
        }
    }
    fun removeExerciseFromPlan(cwiczenie: cwiczeniaPlanuTreningowego ) {
        selectedExercisedOnNewTP.remove(cwiczenie)
        val opt = selectedExercised.find { it.id == cwiczenie.id }
        if (opt != null) {
            selectedExercised.remove(opt)
        }
    }

    var loading by mutableStateOf(false)
    fun downloadTreningDetail() {
        loading = true
        Log.e("downloadTreningDetail", "downloadTreningDetail")
        viewModelScope.launch {
            try {
                val response = ApiClient.getApi(token ?: "").getExerciseTreningPlan(idPlanu)
                if (response.isSuccessful) {
                    message = "Udało się pobrać dane"
                    Log.e("downloadTreningDetail", "message: ${message}")
                    response.body()?.let { it ->
                        selectedExercisedOnNewTP.clear()
                        selectedExercisedOnNewTP.addAll(it.map { item ->
                            Log.e("downloadTreningDetail", "${item.nazwa} - ${item.id} - ${item.grupaMiesniowas} - ${item.serie}")
                            cwiczeniaPlanuTreningowego(
                            id = item.id,
                            nazwa = item.nazwa,
                            grupaMiesniowas = item.grupaMiesniowas,
                            serie = item.serie.toMutableStateList()
                        ) }.toList()
                        )
                    }
                    response.body()?.forEach { item ->
                        Log.e("downloadTreningDetail", "Plan: ${item.nazwa}, serie: ${item.serie.size}")
                    }
                } else {
                    errorMessage = "Błąd dodania planu treningowego do bazy danych: ${response.code()}"
                    Log.e("downloadTreningDetail", "errorMessage: ${errorMessage}")
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
                Log.e("downloadTreningDetail", "errorMessage: ${errorMessage}")
            }
            finally {
                loading = false
            }
        }
    }

    fun updateTreningPlan() {
        val nowy = PlanTreningowy(
            id = idPlanu,
            id_uzytkownia = -1,
            Date = "",
            nazwa = nazwa,
            cwiczeniaPlanuTreningowe = selectedExercisedOnNewTP.toList(),
            cel = GOAL.fromNazwa(cel) ?: GOAL.CONST,
        )
        viewModelScope.launch {
            try {
                val response = ApiClient.getApi(token ?: "").updateTreningPlan(body = nowy, aktualny = aktualny)
                if (response.isSuccessful) {
                    message = "Dodano plan treningowy do bazy danych"
                } else {
                    errorMessage = "Błąd dodania planu treningowego do bazy danych: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

     fun createNewTreningPlan(nazwa: String, aktualny: Boolean = true, cel : GOAL = GOAL.CONST) {
         val nowy = PlanTreningowy(
             id = -1,
             id_uzytkownia = -1,
             Date = "",
             nazwa = nazwa,
             cwiczeniaPlanuTreningowe = selectedExercisedOnNewTP.toList(),
             cel = cel,
         )
         viewModelScope.launch {
             try {
                 val response = ApiClient.getApi(token ?: "").createTreningPlan(body = nowy, aktualny = aktualny)
                 if (response.isSuccessful) {
                     message = "Dodano plan treningowy do bazy danych"
                 } else {
                     errorMessage = "Błąd dodania planu treningowego do bazy danych: ${response.code()}"
                 }
             } catch (e: Exception) {
                 errorMessage = e.localizedMessage
             }
         }
    }

    var treningsPlanCard = mutableStateListOf<treningsPlanCard>()
    fun getUserTreningPlansCard() {
        viewModelScope.launch {
            try {
                val response = ApiClient.getApi(token ?: "").getAllTreningPlans()
                if (response.isSuccessful) {
                    message = "Pobrano listę treningów"

                    response.body()?.let {
                        treningsPlanCard.clear()
                        treningsPlanCard.addAll(it.sortedWith (
                            compareBy <treningsPlanCard>{ !it.isActive }.thenBy { it.endDate }.thenBy { it.seasonName })
                        )
                    }
                } else {
                    errorMessage = "Błąd pobierania listy treningów: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }





    suspend fun createNewExercise(nazwa: String, opis: String, met: Float, grupyMiesniowe: List<GrupaMiesniowa> ) {
        message = null
        val noweCwiczenie = Cwiczenie(nazwa =  nazwa,
            opis = opis,
            met = met,
            grupaMiesniowas = grupyMiesniowe)

        try {
            val response = ApiClient.getApi(token ?: "").createExercise(noweCwiczenie)
            if (response.isSuccessful) {
                message = "Dodano ćwiczenie do bazy danych"
            } else {
                errorMessage = "Błąd dodania ćwiczenie do bazy danych: ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = e.localizedMessage
        }

    }
}