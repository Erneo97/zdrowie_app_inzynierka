package com.example.firstcomposeap.ui.service.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

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
            return values().firstOrNull { it.displayName == name }
        }
    }

}

class JednostkiAdapter : JsonSerializer<Jednostki>, JsonDeserializer<Jednostki> {

    override fun serialize(
        src: Jednostki?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.name) // wysyłamy "GRAM"
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Jednostki {
        val name = json?.asString
        return Jednostki.valueOf(name!!) // przyjmujemy "GRAM" → enum
    }
}
