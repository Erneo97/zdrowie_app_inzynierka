package com.example.firstcomposeap.ui.service

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

     fun createNewTreningPlan(nazwa: String, aktualny: Boolean = true, cel : GOAL = GOAL.CONST) {
         val nowy = PlanTreningowy(
             id = -1,
             id_uzytkownia = -1,
             Date = "",
             nazwa = nazwa,
             cwiczeniaPlanuTreningowe = selectedExercisedOnNewTP.toList(),
             cel = cel,
         )
         Log.e("createNewTreningPlan", "${nowy} - ${selectedExercisedOnNewTP}")
         viewModelScope.launch {
             try {
                 val response = ApiClient.getApi(token ?: "").createTreningPlan(body = nowy, aktualny = aktualny)
                 if (response.isSuccessful) {
                     message = "Dodano ćwiczenie do bazy danych"
                     Log.e("createNewExercise", message ?: "")
                 } else {
                     errorMessage = "Błąd dodania ćwiczenie do bazy danych: ${response.code()}"
                 }
             } catch (e: Exception) {
                 errorMessage = e.localizedMessage
             }
         }
    }

    var treningsPlanCard = mutableStateListOf<treningsPlanCard>(  treningsPlanCard(
        seasonName = "Zimaq 2025",
        startDate = "05.05.25",
        endDate = "01.10.25",
        trainingCount = 55,
        isActive = true,
        goal = "Masa mięśniowa"
    ),
        treningsPlanCard(
            seasonName = "Wiosna 2025",
            startDate = "05.05.25",
            endDate = "01.10.25",
            trainingCount = 333,
            goal = "Masa mięśniowa"
        ),
        treningsPlanCard(
            seasonName = "Wiosna 2025",
            startDate = "05.05.25",
            endDate = "01.10.25",
            trainingCount = 44,
            goal = "Masa mięśniowa"
        ),
        treningsPlanCard(
            seasonName = "Wiosna 2025",
            startDate = "05.05.25",
            endDate = "01.10.25",
            trainingCount = 23,
            goal = "Masa mięśniowa"
        ),
        treningsPlanCard(
            seasonName = "Wiosna 2025",
            startDate = "05.05.25",
            endDate = "01.10.25",
            trainingCount = 66,
            goal = "Masa mięśniowa"
        ),
        treningsPlanCard(
            seasonName = "Wiosna 2025",
            startDate = "05.05.25",
            endDate = "01.10.25",
            trainingCount = 65,
            goal = "Masa mięśniowa"
        ),
        treningsPlanCard(
            seasonName = "Wiosna 2025",
            startDate = "05.05.25",
            endDate = "01.10.25",
            trainingCount = 45,
            goal = "Masa mięśniowa"
        ),
        treningsPlanCard(
            seasonName = "Wiosna 2025",
            startDate = "05.05.25",
            endDate = "01.10.25",
            trainingCount = 332,
            goal = "Masa mięśniowa"
        ))



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
                Log.e("createNewExercise", message ?: "")
            } else {
                errorMessage = "Błąd dodania ćwiczenie do bazy danych: ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = e.localizedMessage
        }

    }
}