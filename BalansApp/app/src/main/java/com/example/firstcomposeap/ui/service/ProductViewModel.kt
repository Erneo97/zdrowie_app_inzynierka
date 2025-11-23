package com.example.firstcomposeap.ui.service

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.ApiClient
import com.example.firstcomposeap.ui.components.getCurrentDate
import com.example.firstcomposeap.ui.components.getFormOnlyDate
import com.example.firstcomposeap.ui.service.data.AllMealsInDay
import com.example.firstcomposeap.ui.service.data.MealGroup
import com.example.firstcomposeap.ui.service.data.MealUpdate
import com.example.firstcomposeap.ui.service.data.PoraDnia
import com.example.firstcomposeap.ui.service.data.Produkt
import com.example.firstcomposeap.ui.service.data.calculateCaloriesInMeal
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)
    var wybranaData by  mutableStateOf(getFormOnlyDate(getCurrentDate()))


    val mealsMap = mutableStateMapOf<PoraDnia, MealGroup>(
        PoraDnia.SNIADANIE to MealGroup(PoraDnia.SNIADANIE, mutableStateListOf()),
        PoraDnia.LUNCH to MealGroup(PoraDnia.LUNCH, mutableStateListOf()),
        PoraDnia.OBIAD to MealGroup(PoraDnia.OBIAD, mutableStateListOf()),
        PoraDnia.KOLACJA to MealGroup(PoraDnia.KOLACJA, mutableStateListOf()),
        PoraDnia.PRZEKASKA to MealGroup(PoraDnia.PRZEKASKA, mutableStateListOf())
    )
    var consumedCalloriesThisDay by mutableStateOf<Double>(0.0)

    fun calculateCalorienOnThisDay() {
        consumedCalloriesThisDay = 0.0

        PoraDnia.entries.forEach { it ->
            if( it != PoraDnia.CLEAR) {
                consumedCalloriesThisDay +=  calculateCaloriesInMeal(mealsMap[it]!!.produkty)
            }
        }
    }

    fun clearListMealsMap( ) {
        mealsMap[PoraDnia.SNIADANIE]!!.produkty.clear()
        mealsMap[PoraDnia.LUNCH]!!.produkty.clear()
        mealsMap[PoraDnia.OBIAD]!!.produkty.clear()
        mealsMap[PoraDnia.KOLACJA]!!.produkty.clear()
        mealsMap[PoraDnia.PRZEKASKA]!!.produkty.clear()
    }

    var selectedProducts =   mutableStateListOf<Produkt>() // wypełniany w widoku szukania produktów (SearchProductScreen za  pośrednictwem zmiennej consumeProduct) przekazywany do widoku posiłków w ciuągu dnia i tam czyszczony dane do dodania usuwanie przez przycisk w oknie
    var isSelectedProductsReadyToSend = mutableStateOf(false)
    var selectedDayTime = mutableStateOf(PoraDnia.CLEAR)

    var selectedRecipes =   mutableStateListOf<List<Produkt>>()
    var isSelectedRecipesReadyToSend = mutableStateOf(false)

    private fun mapAllMealsInDayOnMealsMap(meals : AllMealsInDay) {
        clearListMealsMap ()

        mealsMap[PoraDnia.SNIADANIE]!!.produkty.addAll(meals.sniadanie)
        mealsMap[PoraDnia.LUNCH]!!.produkty.addAll(meals.lunch)
        mealsMap[PoraDnia.OBIAD]!!.produkty.addAll(meals.obiad)
        mealsMap[PoraDnia.KOLACJA]!!.produkty.addAll(meals.kolacja)
        mealsMap[PoraDnia.PRZEKASKA]!!.produkty.addAll(meals.inne)

        Log.e("mapAllMealsInDayOnMealsMap", "${mealsMap[PoraDnia.SNIADANIE]!!.produkty.size} - " +
                "${mealsMap[PoraDnia.LUNCH]!!.produkty.size} - ${mealsMap[PoraDnia.OBIAD]!!.produkty.size}" +
            "${mealsMap[PoraDnia.KOLACJA]!!.produkty.size} - ${mealsMap[PoraDnia.PRZEKASKA]!!.produkty.size}")

    }

    var isLoadedMeals by mutableStateOf(false)
    suspend fun downloadMealUserDay() {
        Log.e("downloadMealUserDay", "start")
        isLoadedMeals = false
        try {
            val response = ApiClient.getApi(token ?: "").getUserMealDay(wybranaData)
            if (response.isSuccessful) {
                val meals = response.body()
                if (meals != null) {
                    message = "Pobrano listę posiłków"
                    Log.e("downloadMealUserDay", meals.toString())
                    mapAllMealsInDayOnMealsMap(meals)
                } else {
                    errorMessage = "Błąd: odpowiedź pusta"
                }
            } else {
                errorMessage = "Pobranie posiłków nie powiodło się: ${response.code()}"
            }

        } catch (e: Exception) {
            errorMessage = "Błąd sieci: ${e.localizedMessage}"
        }
        finally {
            isLoadedMeals = true
        }

    }

    fun updateUserMeal() {
        val update = MealUpdate(
            meal = mealsMap[selectedDayTime.value]!!.produkty,
            data = wybranaData,
            poraDnia = selectedDayTime.value
        )
        viewModelScope.launch {
            try {
                Log.d("token", "addNewProduct ${token}")
                val response = ApiClient.getApi(token ?: "").createOrUpdateMeal(update)
                if (response.isSuccessful) {
                    message = "Aktualizacja twoich posiłków"
                } else {
                    errorMessage = "Błąd aktualizacji posiłku: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }

    }

    var selectedUserToEditMeal by   mutableStateOf("")

    suspend fun downloadMealAnotherUserDay() {
        Log.e("downloadMealUserDay", "start")
        isLoadedMeals = false
        val email = selectedUserToEditMeal.substringBefore(" - ").trim()
        try {
            val response = ApiClient.getApi(token ?: "").getMealAnotherUser(wybranaData, email)
            if (response.isSuccessful) {
                val meals = response.body()
                if (meals != null) {
                    message = "Pobrano listę posiłków"
                    Log.e("downloadMealUserDay", meals.toString())
                    mapAllMealsInDayOnMealsMap(meals)
                } else {
                    errorMessage = "Błąd: odpowiedź pusta"
                }
            } else {
                errorMessage = "Pobranie posiłków nie powiodło się: ${response.code()}"
            }

        } catch (e: Exception) {
            errorMessage = "Błąd sieci: ${e.localizedMessage}"
        }
        finally {
            isLoadedMeals = true
        }

    }

    fun updateAnotherUserMeal() {
        val update = MealUpdate(
            meal = mealsMap[selectedDayTime.value]!!.produkty,
            data = wybranaData,
            poraDnia = selectedDayTime.value
        )
        val email = selectedUserToEditMeal.substringBefore(" - ").trim()
        viewModelScope.launch {
            try {
                Log.d("token", "addNewProduct ${token}")
                val response = ApiClient.getApi(token ?: "").createOrUpdateAnotherUserMeal(update, email)
                if (response.isSuccessful) {
                    message = "Aktualizacja twoich posiłków"
                } else {
                    errorMessage = "Błąd aktualizacji posiłku: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }

    }

    fun addNewProduct(product: Produkt) {
        viewModelScope.launch {
            try {
                Log.d("token", "addNewProduct ${token}")
                val response = ApiClient.api.addProductToDb(product)
                if (response.isSuccessful) {
                    message = "Dodano produkt do bazy danych"
                } else {
                    errorMessage = "Błąd dodania produktu do bazy danych: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    }

    var foundProduct by mutableStateOf<Produkt?>(null)
    var consumedProduct by mutableStateOf<Produkt?>(null)



    fun getProductById(id: Int) {
        Log.e("getProductById", "${id}")
        viewModelScope.launch {
            try {
                val response = ApiClient.api.findProductById(id)

                if (response.isSuccessful) {

                    foundProduct = response.body()
                    Log.e("getProductById", "${foundProduct}")
                    errorMessage = null
                } else {
                    errorMessage = "Błąd pobierania produktu: ${response.code()}"
                    foundProduct = null
                }

            } catch (e: Exception) {
                errorMessage = e.localizedMessage
                foundProduct = null
            }
        }
    }

}