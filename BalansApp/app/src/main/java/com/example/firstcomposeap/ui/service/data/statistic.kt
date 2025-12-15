package com.example.firstcomposeap.ui.service.data

data class StatisticInterval(
    var data: String,
    var countDays: Int
)


data class StatisticParameters(
    var min: Double = 0.0,
    var max: Double = 0.0,
    var average: Double = 0.0,
    var median: Double = 0.0,
    var a: Double,
    var b: Double
)
