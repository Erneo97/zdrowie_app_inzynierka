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
import com.example.balansapp.ui.service.data.PommiarWagii
import com.example.firstcomposeap.ui.service.data.ChartPoint
import com.example.firstcomposeap.ui.service.data.PomiarWagiOptions
import com.example.firstcomposeap.ui.service.data.StatisticInterval
import com.example.firstcomposeap.ui.service.data.StatisticParameters
import kotlinx.coroutines.launch
import java.time.LocalDate

class StatisticViewModel : ViewModel() {
    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)

    var weightStats: SnapshotStateList<StatisticParameters?> = mutableStateListOf()
    var weightData: SnapshotStateList<PommiarWagii> = mutableStateListOf()

    fun getCorrectStatisticParameters(option: PomiarWagiOptions) : StatisticParameters {
        if( weightStats.isEmpty() )
            return StatisticParameters(
                min = 0.0,
                max = 0.0,
                average = 0.0,
                median = 0.0,
                a = 0.0,
                b = 0.0
            )

        return weightStats[option.indexList]!!
    }

    fun downloadUserStatistic(days: Int = 90, date: LocalDate = LocalDate.now()) {
        downloadWeightsUserStatistic(days, date)

        downloadWeightsDataUser(days, date)

    }

    var caloriesData: SnapshotStateList<ChartPoint> = mutableStateListOf()
    fun downloadCaloriesUserStatistic(days: Int = 90, date: LocalDate = LocalDate.now()) {
        val internal = StatisticInterval(
            data = date.toString(),
            countDays = days
        )
        viewModelScope.launch {
            try {
                val response = ApiClient.getApi(token ?: "").getUserCalories(
                    date = date.toString(),
                    countDays =  days
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        caloriesData.clear()
                        caloriesData = it.toMutableStateList()
                    }
                } else {
                    errorMessage = "Błąd dodania rekordu wagii: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

    fun downloadWeightsUserStatistic(days: Int = 90, date: LocalDate = LocalDate.now()) {
        val internal = StatisticInterval(
            data = date.toString(),
            countDays = days
        )
        viewModelScope.launch {
            try {
                val response = ApiClient.getApi(token ?: "").getStatisticUserWeight( interval = internal)
                if (response.isSuccessful) {
                    response.body()?.let {
                        weightStats.clear()
                        weightStats = it.toMutableStateList()
                    }
                } else {
                    errorMessage = "Błąd dodania rekordu wagii: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

    var loaded by mutableStateOf(false)
    fun downloadWeightsDataUser(days: Int = 90, date: LocalDate = LocalDate.now()) {
        Log.e("downloadWeightsDataUser", "${date} ${days} ${token ?: "brak token"}")
        loaded = false
        viewModelScope.launch {
            try {
                val response = ApiClient.getApi(token ?: "").getcUserWeights(
                    date = date.toString(),
                    countDays =  days
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        weightData.clear()
                        weightData = it.toMutableStateList()
                        weightData.forEach { it ->
                            it.data = it.data.substring(0,10)
                        }
                    }
                } else {
                    errorMessage = "Błąd dodania rekordu wagii: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
            finally {
                loaded = true
            }
        }
    }

    fun getDataByOption(option: PomiarWagiOptions) : List<ChartPoint> {
        if( weightData.isEmpty() )
            return emptyList()

        if( option == PomiarWagiOptions.TK_MIESNIOWA ) {
            return weightData.filter { it.miesnie > 0.0 }.mapIndexed {  index, it -> ChartPoint(
                x = it.data,
                y = it.miesnie
            ) }
        }

        if( option == PomiarWagiOptions.TK_TLUSZCZOWA ) {
            return weightData.filter { it.tluszcz > 0.0 }.map{  it -> ChartPoint(
                x = it.data,
                y = it.tluszcz
            ) }
        }

        if( option == PomiarWagiOptions.NAWODNIENIE ) {
            return weightData.filter { it.nawodnienie > 0.0 }.mapIndexed {  index, it -> ChartPoint(
                x = it.data,
                y = it.nawodnienie
            ) }
        }

        return weightData.filter { it.wartosc > 0.0 }.mapIndexed {  index, it -> ChartPoint(
            x = it.data,
            y = it.wartosc
        ) }
    }

}