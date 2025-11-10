package com.example.firstcomposeap.ui.service.data

data class MealInfo (
    val id: Int,    // id produktu w bazie danych
    val nazwa: String,
    var dawka: Dawka
)

data class Dawka (
    val jednostka: JEDNOSTKA,
    val wartosc: Double,
    val kcal: Float,
    val bialko: Float,
    val weglowodany: Float,
    val tluszcze : Float,
    val blonnik: Float
)

enum class JEDNOSTKA (val displayName: String) {
    LITR("l."),
    GRAM("g."),
    KILOGRAM("kg."),
    SZTUKI("szt."),
    MILILITR("ml.");

    companion object {
        fun fromDisplayName(name: String): JEDNOSTKA? {
            return values().firstOrNull { it.displayName == name }
        }
    }
}