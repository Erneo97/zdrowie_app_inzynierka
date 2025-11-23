package com.example.firstcomposeap.ui.service.data

import androidx.compose.runtime.snapshots.SnapshotStateList

data class MealInfo (
    val id: Long,    // id produktu w bazie danych
    val nazwa: String,
    val producent: String,
    val kodKreskowy: String,
    var objetosc: Dawka
)

fun calculateCaloriesInMeal(meals: List<MealInfo>) : Double {
    var sum = 0.0
    meals.forEach { meal -> sum = sum + meal.objetosc.kcal }

    return sum
}

data class AllMealsInDay(
    var sniadanie: List<MealInfo> = emptyList(),
    var lunch: List<MealInfo> = emptyList(),
    var obiad: List<MealInfo> = emptyList(),
    var kolacja: List<MealInfo> = emptyList(),
    var inne: List<MealInfo> = emptyList()
)

data class MealGroup(
    val poraDnia: PoraDnia,
    val produkty: SnapshotStateList<MealInfo>
)

fun Produkt.toMealInfo(): MealInfo {
    return MealInfo(
        id = this.id,
        nazwa = this.nazwa,
        producent = this.producent,
        kodKreskowy = this.kodKreskowy,
        objetosc = this.objetosc.first()
    )
}

fun List<Produkt>.toMealInfoList(): List<MealInfo> {
    return this.map { it.toMealInfo() }
}


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
    var kcal: Float,
    var bialko: Float,
    var weglowodany: Float,
    var tluszcze : Float,
    var blonnik: Float
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
    PRZEKASKA("Przekąska"),
    CLEAR("");

    companion object {
        fun fromDisplayName(name: String): PoraDnia =
            entries.firstOrNull { it.displayName == name } ?: CLEAR

        fun fromName(name: String): PoraDnia =
            entries.firstOrNull { it.name == name } ?: CLEAR
    }
}


// WSZYSTKIE DANE jakie znajdują się w danym dniu i porze w posiłku użytkownika
data class MealUpdate (
    val meal: List<MealInfo>,
    val data: String,
    val poraDnia: PoraDnia
)