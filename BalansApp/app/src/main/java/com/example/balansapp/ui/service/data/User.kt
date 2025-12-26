package com.example.balansapp.ui.service.data

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.firstcomposeap.ui.service.data.MealInfo
import com.example.firstcomposeap.ui.service.data.Produkt


data class Uzytkownik(
    var id: Int = 0,
    var aktualnyPlan: Int = 0,
    var imie: String,
    var nazwisko: String,
    var email: String,
    val haslo: String,
    var waga: List<PommiarWagii> = emptyList(),
    var wzrost: Int = 0,
    var plec: Plec,
    val role: String = "",
    var dataUrodzenia: String,
    var przyjaciele: List<Przyjaciele> = emptyList(),
    var dania: List<Dania> = emptyList(),
    var zapotrzebowanieKcal: Int
)

data class Dania(
    val id: Int,
    val nazwa: String = ""
)

data class DaniaDetail(
    val id: Int,
    var nazwa: String = "",
    var listaProdukty: List<Produkt>
)

fun DaniaDetail.toMealInfoList(): List<MealInfo> {
    return listaProdukty.map { produkt ->
        MealInfo(
            id = produkt.id,
            nazwa = produkt.nazwa,
            producent = produkt.producent,
            kodKreskowy = produkt.kodKreskowy,
            objetosc = produkt.objetosc.firstOrNull()
                ?: throw IllegalStateException("Produkt ${produkt.nazwa} nie ma żadnych dawek!")
        )
    }
}

fun <T> SnapshotStateList<T>.replaceWith(newList: List<T>) {
    this.clear()
    this.addAll(newList)
}


data class PommiarWagii(
    var wartosc: Double,
    var data: String,
    val tluszcz: Double,
    val miesnie: Double,
    val nawodnienie: Double
)


data class Przyjaciele(
    val id: Int,
    val czyDozwolony: Boolean,
    val czyPrzyjaciel: Boolean
)

data class ZaproszenieInfo (
    val id: Int,
    val imie: String,
    val nazwisko: String,
    val email: String,
    val creator: Boolean,
)

data class PrzyjacieleInfo (
    val id: Int,
    val imie: String,
    val nazwisko: String,
    val email: String,
    var czyDozwolony: Boolean
)