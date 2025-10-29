package com.example.balansapp.ui.service.data

import java.util.Date


data class Uzytkownik(
    val id: Int = 0,
    val aktualnyPlan: Int = 0,
    val imie: String,
    val nazwisko: String,
    val email: String,
    val haslo: String,
    val waga: List<PommiarWagii> = emptyList(),
    val wzrost: Int = 0,
    val plec: Plec,
    val przyjaciele: List<Przyjaciele> = emptyList(),
    val dania: List<Dania> = emptyList()
)

data class Dania(
    val id: Int,
    val nazwa: String = ""
)

data class PommiarWagii(
    val wartosc: Int,
    val data: Date
)


data class Przyjaciele(
    val id: Int,
    val czyDozwolony: Boolean,
    val czyPrzyjaciel: Boolean
)