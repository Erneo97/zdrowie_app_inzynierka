package com.example.firstcomposeap.ui.service.data

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList

data class treningsPlanCard(
    val seasonName : String,
    val startDate : String,
    val endDate : String,
    val trainingCount : Int,
    val isActive : Boolean = false,
    val goal : String
)

data class PlanTreningowy(
    val id: Int,
    val id_uzytkownia: Int,
    val Date: String,
    val nazwa : String,
    val cwiczeniaPlanuTreningowe : List<cwiczeniaPlanuTreningowego>,
    val cel : GOAL
)

enum class GOAL(val label: String) {
    REDUCE("Redukcja wagi"),

    MUSCLE("Masa mięśniowa"),

    CONST("Utrzymanie wagi");


    companion object {
        fun fromNazwa(label: String  ) : GOAL? {
            return GOAL.entries.find { it.label.equals(label, ignoreCase = true) }
        }

    }
}

data class Cwiczenie(
    val id: Int = -1,
    val nazwa: String = "",
    val opis: String = "",
    val grupaMiesniowas: List<GrupaMiesniowa> = emptyList(),
    val met: Float = 0f
)

data class cwiczeniaPlanuTreningowego (
    val id: Int,
    val nazwa: String,
    val grupaMiesniowas: List<GrupaMiesniowa>,
    val serie: SnapshotStateList<Seria> = emptyList<Seria>().toMutableStateList()
)

data class Seria(
    var liczbaPowtorzen: Int = 0,
    var obciazenie: Float = 0.0f,
)



enum class GrupaMiesniowa(val grupaNazwa: String) {

    // --- GŁÓWNE GRUPY ---
    NOGI("Nogi"),
    PLECY("Plecy"),
    BICEPS("Biceps"),
    TRICEPS("Triceps"),
    BARKI("Barki"),
    KLATKA_PIERSIOWA("Klatka piersiowa"),
    BRZUCH("Brzuch"),
    PRZEDRAMIE("Przedramię"),
    LDYKI("Łydki"),
    KAPTURY("Kaptury"),

    // --- NOGI - PODGRUPY ---
    NOGI_CZWOROGLOWE("Nogi - czworogłowe uda"),
    NOGI_DWUGLOWE("Nogi - dwugłowe uda"),
    NOGI_POSLADKI("Nogi - pośladki"),
    NOGI_PRZYWODZICIELE("Nogi - przywodziciele"),
    NOGI_ODWODZICIELE("Nogi - odwodziciele"),

    // --- PLECY - PODGRUPY ---
    PLECY_NAJSZERSZY("Plecy - najszeroki grzbietu"),
    PLECY_PROSTOWNIKI("Plecy - prostowniki grzbietu"),
    PLECY_OBLE("Plecy - mięśnie obłe"),
    PLECY_RÓWNOLEGLOBOCZNE("Plecy - równoległoboczne"),
    PLECY_TRAPEZ_SRODKOWY_DOLNY("Plecy - czworoboczny środkowy/dolny"),

    // --- BICEPS - PODGRUPY ---
    BICEPS_GLOWA_DLUGA("Biceps - głowa długa"),
    BICEPS_GLOWA_KROTKA("Biceps - głowa krótka"),
    BICEPS_RAMIENNY("Biceps - mięsień ramienny"),

    // --- TRICEPS - PODGRUPY ---
    TRICEPS_GLOWA_DLUGA("Triceps - głowa długa"),
    TRICEPS_GLOWA_BOCZNA("Triceps - głowa boczna"),
    TRICEPS_GLOWA_PRZYSRODKOWA("Triceps - głowa przyśrodkowa"),

    // --- BARKI - PODGRUPY ---
    BARKI_PRZEDNI("Barki - akton przedni"),
    BARKI_SRODKOWY("Barki - akton środkowy"),
    BARKI_TYLNY("Barki - akton tylny"),

    // --- KLATKA PIERSIOWA - PODGRUPY ---
    KLATKA_GORNA("Klatka piersiowa - górna"),
    KLATKA_SRODKOWA("Klatka piersiowa - środkowa"),
    KLATKA_DOLNA("Klatka piersiowa - dolna"),

    // --- BRZUCH - PODGRUPY ---
    BRZUCH_PROSTY("Brzuch - prosty brzucha"),
    BRZUCH_SKOSNE("Brzuch - skośne brzucha"),
    BRZUCH_POPRZECZNY("Brzuch - poprzeczny brzucha"),

    // --- PRZEDRAMIĘ - PODGRUPY ---
    PRZEDRAMIE_ZGINACZE("Przedramię - zginacze"),
    PRZEDRAMIE_PROSTOWNIKI("Przedramię - prostowniki"),
    PRZEDRAMIE_ROTATORY("Przedramię - rotatory"),

    // --- ŁYDKI - PODGRUPY ---
    LDYKI_BRZUCHATY("Łydki - mięsień brzuchaty łydki"),
    LDYKI_PLASZCZKOWATY("Łydki - mięsień płaszczkowaty"),


    CARDIO("Cardio");

    companion object {
        fun fromNazwa(nazwa: String  ) : GrupaMiesniowa? {
            return GrupaMiesniowa.entries.find { it.grupaNazwa.equals(nazwa, ignoreCase = true) }
        }

    }

}
