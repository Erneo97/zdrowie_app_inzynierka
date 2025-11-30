package com.example.firstcomposeap.ui.service.data

data class treningsPlanCard(
    val seasonName : String,
    val startDate : String,
    val endDate : String,
    val trainingCount : Int,
    val isActive : Boolean = false,
    val goal : String
)