package com.example.firstcomposeap.ui.service.data

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList

data class treningsPlanCard(
    val id: Int,
    val seasonName : String,
    val startDate : String,
    val endDate : String,
    val trainingCount : Int,
    val isActive : Boolean = false,
    val goal : GOAL
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

data class cwiczeniaPlanuTreningowegoResponse (
    val id: Int,
    val nazwa: String,
    val grupaMiesniowas: List<GrupaMiesniowa>,
    val serie: List<Seria> = emptyList()
)

data class Seria(
    var liczbaPowtorzen: Int = 0,
    var obciazenie: Float = 0.0f,
)



enum class GrupaMiesniowa(
    val grupaNazwa: String,
    val parent: GrupaMiesniowa? = null
) {

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
    CARDIO("Cardio"),

    // --- NOGI - PODGRUPY ---
    NOGI_CZWOROGLOWE("Nogi - czworogłowe uda", NOGI),
    NOGI_DWUGLOWE("Nogi - dwugłowe uda", NOGI),
    NOGI_POSLADKI("Nogi - pośladki", NOGI),
    NOGI_PRZYWODZICIELE("Nogi - przywodziciele", NOGI),
    NOGI_ODWODZICIELE("Nogi - odwodziciele", NOGI),

    // --- PLECY - PODGRUPY ---
    PLECY_NAJSZERSZY("Plecy - najszeroki grzbietu", PLECY),
    PLECY_PROSTOWNIKI("Plecy - prostowniki grzbietu", PLECY),
    PLECY_OBLE("Plecy - mięśnie obłe", PLECY),
    PLECY_RÓWNOLEGLOBOCZNE("Plecy - równoległoboczne", PLECY),
    PLECY_TRAPEZ_SRODKOWY_DOLNY("Plecy - czworoboczny środkowy/dolny", PLECY),

    // --- BICEPS - PODGRUPY ---
    BICEPS_GLOWA_DLUGA("Biceps - głowa długa", BICEPS),
    BICEPS_GLOWA_KROTKA("Biceps - głowa krótka", BICEPS),
    BICEPS_RAMIENNY("Biceps - mięsień ramienny", BICEPS),

    // --- TRICEPS - PODGRUPY ---
    TRICEPS_GLOWA_DLUGA("Triceps - głowa długa", TRICEPS),
    TRICEPS_GLOWA_BOCZNA("Triceps - głowa boczna", TRICEPS),
    TRICEPS_GLOWA_PRZYSRODKOWA("Triceps - głowa przyśrodkowa", TRICEPS),

    // --- BARKI - PODGRUPY ---
    BARKI_PRZEDNI("Barki - akton przedni", BARKI),
    BARKI_SRODKOWY("Barki - akton środkowy", BARKI),
    BARKI_TYLNY("Barki - akton tylny", BARKI),

    // --- KLATKA PIERSIOWA - PODGRUPY ---
    KLATKA_GORNA("Klatka piersiowa - górna", KLATKA_PIERSIOWA),
    KLATKA_SRODKOWA("Klatka piersiowa - środkowa", KLATKA_PIERSIOWA),
    KLATKA_DOLNA("Klatka piersiowa - dolna", KLATKA_PIERSIOWA),

    // --- BRZUCH - PODGRUPY ---
    BRZUCH_PROSTY("Brzuch - prosty brzucha", BRZUCH),
    BRZUCH_SKOSNE("Brzuch - skośne brzucha", BRZUCH),
    BRZUCH_POPRZECZNY("Brzuch - poprzeczny brzucha", BRZUCH),

    // --- PRZEDRAMIĘ - PODGRUPY ---
    PRZEDRAMIE_ZGINACZE("Przedramię - zginacze", PRZEDRAMIE),
    PRZEDRAMIE_PROSTOWNIKI("Przedramię - prostowniki", PRZEDRAMIE),
    PRZEDRAMIE_ROTATORY("Przedramię - rotatory", PRZEDRAMIE),

    // --- ŁYDKI - PODGRUPY ---
    LDYKI_BRZUCHATY("Łydki - mięsień brzuchaty łydki", LDYKI),
    LDYKI_PLASZCZKOWATY("Łydki - mięsień płaszczkowaty", LDYKI);


    fun isMain(): Boolean = parent == null

    fun getMainGroup(): GrupaMiesniowa = parent ?: this

    companion object {
        fun fromNazwa(nazwa: String): GrupaMiesniowa? =
            entries.find { it.grupaNazwa.equals(nazwa, ignoreCase = true) }
    }
}



data class Trening(
    var idTrening: Int,
    var idUser: Int,
    var idPlanu: Int,
    var data: String,
    var cwiczenia: SnapshotStateList <CwiczenieWTreningu>,
    var spaloneKalorie: Float
)

data class TreningCardInformation(
    var idTrening: Int,
    var nazwa: String,
    var iloscCwiczen: Int,
    var date: String,
    var spaloneKalorie: Float = 0f
)

data class CwiczenieWTreningu(
    var id: Int,
    var nazwa: String,
    var czas: String, // format mm:ss
    var serie: SnapshotStateList<Seria>,
    val met: Float
)