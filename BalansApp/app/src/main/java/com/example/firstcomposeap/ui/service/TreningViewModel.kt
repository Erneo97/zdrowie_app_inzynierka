package com.example.firstcomposeap.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.firstcomposeap.ui.service.data.treningsPlanCard

class TreningViewModel : ViewModel() {
    var token by mutableStateOf<String?>(null)

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



}