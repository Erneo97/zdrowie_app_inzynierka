package com.example.firstcomposeap.ui.service.data

data class MealInfo (
    val id: Long,    // id produktu w bazie danych
    val nazwa: String,
    val producent: String,
    val kodKreskowy: String,
    var objetosc: Dawka
)

data class Product (
    val id: Long,    // id produktu w bazie danych
    val nazwa: String,
    val producent: String,
    val kodKreskowy: String,
    var objetosc: List<Dawka>
)

data class Dawka (
    val jednostka: JEDNOSTKA,
    val wartosc: Float,
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