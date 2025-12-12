package com.example.firstcomposeap.ui.service

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.balansapp.ui.service.ApiClient
import com.example.firstcomposeap.ui.service.data.Cwiczenie
import com.example.firstcomposeap.ui.service.data.GrupaMiesniowa
import com.example.firstcomposeap.ui.service.data.treningsPlanCard


class TreningViewModel : ViewModel() {
    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)

    var selectedExercisedOnNewTP = mutableStateListOf<Cwiczenie>()

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