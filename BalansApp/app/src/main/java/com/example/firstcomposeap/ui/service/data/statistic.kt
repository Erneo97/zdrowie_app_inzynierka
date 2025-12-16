package com.example.firstcomposeap.ui.service.data

enum class StatisticPeriod(val label: String, val days: Int) {
    WEEK("1 tydzień", 7),
    MONTH("Miesiąc", 30),
    SIX_MONTHS("6 miesięcy", 180),
    YEAR("Rok", 365)
}

enum class PomiarWagiOptions(val label: String, val indexList: Int) {
    WAGA("waga", 0),
    TK_MIESNIOWA("Tk. mięśniowa", 1),
    TK_TLUSZCZOWA("Th. tłuszczowa", 2),
    NAWODNIENIE("Nawodnienie", 3)
}

data class StatisticInterval(
    var data: String,
    var countDays: Int
)

data class ChartPoint(
    val x: Double,
    val y: Double
)

data class StatisticParameters(
    var min: Double = 0.0,
    var max: Double = 0.0,
    var average: Double = 0.0,
    var median: Double = 0.0,
    var a: Double,
    var b: Double
)
