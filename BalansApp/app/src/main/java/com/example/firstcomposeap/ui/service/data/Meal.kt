package com.example.firstcomposeap.ui.service.data


data class MealInfo (
    val id: Long,    // id produktu w bazie danych
    val nazwa: String,
    val producent: String,
    val kodKreskowy: String,
    var objetosc: Dawka
)

data class Produkt (
    val id: Long,    // id produktu w bazie danych
    val nazwa: String,
    val producent: String,
    val kodKreskowy: String,
    var objetosc: List<Dawka>
)

data class Dawka (
    val jednostki: Jednostki ,
    val wartosc: Float,
    val kcal: Float,
    val bialko: Float,
    val weglowodany: Float,
    val tluszcze : Float,
    val blonnik: Float
)

enum class Jednostki  (val displayName: String) {
    LITR("l."),
    GRAM("g."),
    KILOGRAM("kg."),
    SZTUKI("szt."),
    MILILITR("ml.");



    companion object {
        fun fromDisplayName(name: String): Jednostki ? {
            return Jednostki.entries.firstOrNull { it.displayName == name }
        }
    }

}

enum class PoraDnia(val displayName: String) {
    SNIADANIE("Śniadanie"),
    LUNCH("Lunch"),
    OBIAD("Obiad"),
    KOLACJA("Kolacja"),
    PRZEKASKA("Przekąska")
}


// WSZYSTKIE DANE jakie znajdują się w danym dniu i porze w posiłku użytkownika
data class MealUpdate (
    val meal: List<Produkt>,
    val data: String,
    val poraDnia: PoraDnia
)