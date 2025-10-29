package com.example.balansapp.ui.service.data

data class RegisterRequest(
    val imie: String,
    val nazwisko: String,
    val email: String,
    val haslo: String,
    val plec: String
)

enum class Plec (val displayName: String) {
    MEZCZYZNA("MEZCZYZNA"),
    KOBIETA("KOBIETA");

    companion object {
        // Funkcja do konwersji z wyświetlanej nazwy na enum
        fun fromDisplayName(name: String): Plec? {
            return values().firstOrNull { it.displayName == name }
        }
    }
}

data class RegisterResponse(
    val id: Int,
    val imie: String,
    val nazwisko: String,
    val email: String,
    val wzrost: Int,
    val plec: Plec
)