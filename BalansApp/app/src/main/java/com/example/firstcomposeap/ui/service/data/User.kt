package com.example.balansapp.ui.service.data

import java.util.Date


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
    var dataUrodzenia: String,
    var przyjaciele: List<Przyjaciele> = emptyList(),
    var dania: List<Dania> = emptyList(),
    var zapotrzebowanieKcal: Int
)

data class Dania(
    val id: Int,
    val nazwa: String = ""
)

data class PommiarWagii(
    var wartosc: Double,
    val data: String,
    val tluszcz: Double,
    val miesnie: Double,
    val nawodnienie: Double
)


data class Przyjaciele(
    val id: Int,
    val czyDozwolony: Boolean,
    val czyPrzyjaciel: Boolean
)