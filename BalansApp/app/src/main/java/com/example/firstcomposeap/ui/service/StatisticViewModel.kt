package com.example.firstcomposeap.ui.service

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.ApiClient
import com.example.firstcomposeap.ui.service.data.StatisticInterval
import com.example.firstcomposeap.ui.service.data.StatisticParameters
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class StatisticViewModel : ViewModel() {
    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)

    var weightStats: SnapshotStateList<StatisticParameters?> = mutableStateListOf()

    fun downloadWeightsUserStatistic() {
        Log.e("downloadWeightsUserStatistic", "called ${token ?: "brak token"}")

        val internal = StatisticInterval(
            data = "2025-10-25",
            countDays = 35
        )
        viewModelScope.launch {
            try {
                val response = ApiClient.getApi(token ?: "").getStatisticUserWeight( interval = internal)
                if (response.isSuccessful) {
                    response.body()?.let { weightStats = it.toMutableStateList()
                        Log.e("downloadWeightsUserStatistic", "weightStats")
                    }
                    Log.e("downloadWeightsUserStatistic", "succes")
                } else {
                    errorMessage = "Błąd dodania rekordu wagii: ${response.code()}"
                    Log.e("downloadWeightsUserStatistic", "error:  ${response.code()}")
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

}