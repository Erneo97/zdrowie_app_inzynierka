package com.example.firstcomposeap.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.ApiClient
import com.example.balansapp.ui.service.data.DaniaDetail
import com.example.firstcomposeap.ui.components.getCurrentDate
import com.example.firstcomposeap.ui.components.getFormOnlyDate
import com.example.firstcomposeap.ui.service.data.AllMealsInDay
import com.example.firstcomposeap.ui.service.data.Dawka
import com.example.firstcomposeap.ui.service.data.Jednostki
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
    var selectedTabIndex by mutableIntStateOf(0) // wybieranie która zakłada użytkownik / przyjaciel

    val mealsMap = mutableStateMapOf(
        PoraDnia.SNIADANIE to MealGroup(PoraDnia.SNIADANIE, mutableStateListOf()),
        PoraDnia.LUNCH to MealGroup(PoraDnia.LUNCH, mutableStateListOf()),
        PoraDnia.OBIAD to MealGroup(PoraDnia.OBIAD, mutableStateListOf()),
        PoraDnia.KOLACJA to MealGroup(PoraDnia.KOLACJA, mutableStateListOf()),
        PoraDnia.PRZEKASKA to MealGroup(PoraDnia.PRZEKASKA, mutableStateListOf())
    )
    var consumedCalloriesThisDay by mutableDoubleStateOf(0.0)

    var selectedTabIndexProductRecipe by mutableIntStateOf(0)  // zapamiętanie która zakładka w serach screen jest wybrana Produkty czy Danie

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
    var isSelectedRecipesReadyToSend = mutableStateOf(false)

    private fun mapAllMealsInDayOnMealsMap(meals : AllMealsInDay) {
        clearListMealsMap ()

        mealsMap[PoraDnia.SNIADANIE]!!.produkty.addAll(meals.sniadanie)
        mealsMap[PoraDnia.LUNCH]!!.produkty.addAll(meals.lunch)
        mealsMap[PoraDnia.OBIAD]!!.produkty.addAll(meals.obiad)
        mealsMap[PoraDnia.KOLACJA]!!.produkty.addAll(meals.kolacja)
        mealsMap[PoraDnia.PRZEKASKA]!!.produkty.addAll(meals.inne)
    }

    var isLoadedMeals by mutableStateOf(false)
    suspend fun downloadMealUserDay() {
        isLoadedMeals = false
        try {
            val response = ApiClient.getApi(token ?: "").getUserMealDay(wybranaData)
            if (response.isSuccessful) {
                val meals = response.body()
                if (meals != null) {
                    message = "Pobrano listę posiłków"
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
            calculateAllStatistic()
            isLoadedMeals = true
        }

    }

    var makroMeal = mutableStateOf(Dawka(
        jednostki = Jednostki.SZTUKI,
        wartosc = 0.0f,
        kcal = 0.0f,
        bialko = 0.0f,
        weglowodany = 0.0f,
        tluszcze = 0.0f,
        blonnik = 0.0f
    ))


    fun calculateAllStatistic() {
        calculateCalorienOnThisDay()
        calculateMakroMeals()
    }

    /**
     * Liczy spożycie kcal użytkownika w posiłach w danym dniu
     */
    private fun calculateCalorienOnThisDay() {
        consumedCalloriesThisDay = 0.0

        PoraDnia.entries.forEach { it ->
            if( it != PoraDnia.CLEAR) {
                consumedCalloriesThisDay +=  calculateCaloriesInMeal(mealsMap[it]!!.produkty)
            }
        }
    }

    /**
     * Liczy makro (białko, weglowodany, błonnik, tłuszcze) dla całego posiłku użytkownika w danym dniu
     */
    private fun calculateMakroMeals() {
        makroMeal.value.bialko = 0.0f
        makroMeal.value.weglowodany = 0.0f
        makroMeal.value.tluszcze = 0.0f
        makroMeal.value.blonnik = 0.0f

        PoraDnia.entries.forEach { it ->
            if( it != PoraDnia.CLEAR) {
                mealsMap[it]!!.produkty.forEach { meal ->
                    makroMeal.value.bialko += meal.objetosc.bialko
                    makroMeal.value.weglowodany += meal.objetosc.weglowodany
                    makroMeal.value.tluszcze += meal.objetosc.tluszcze
                    makroMeal.value.blonnik += meal.objetosc.blonnik
                }

            }
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
        isLoadedMeals = false
        val email = selectedUserToEditMeal.substringBefore(" - ").trim()
        try {
            val response = ApiClient.getApi(token ?: "").getMealAnotherUser(wybranaData, email)
            if (response.isSuccessful) {
                val meals = response.body()
                if (meals != null) {
                    message = "Pobrano listę posiłków"
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
            calculateAllStatistic()
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
    var consumedProduct by mutableStateOf<Produkt?>(null) // ProductConsumeDetail dodanie nowego produktu po zmiane jego wartosci



    fun getProductById(id: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.api.findProductById(id)

                if (response.isSuccessful) {

                    foundProduct = response.body()
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


//  Dodawanie produktów do przepisu (nowego)

    var selectedProductsFromRecipe =   mutableStateListOf<Produkt>() // lista produktów w nowym przepisie
    var recipeName = mutableStateOf("")
    var indexRecipe = mutableStateOf(-1)

    fun initEditRecipe( recipe: DaniaDetail ) {
        selectedProductsFromRecipe.clear()
        selectedProductsFromRecipe.addAll(recipe.listaProdukty)
        recipeName.value = recipe.nazwa
        indexRecipe.value = recipe.id
    }

    var isChangeOnRecipe = mutableStateOf(false)

    fun clearCreateProduct() {
        selectedProductsFromRecipe.clear()
        isChangeOnRecipe.value = false
        clearEditRecipe()
    }
    private val cleartedDanie = DaniaDetail(
        id = -1,
        nazwa = "",
        listaProdukty = listOf(Produkt(
            id = -1,
            nazwa = "",
            producent = "",
            kodKreskowy = "",
            objetosc = listOf(Dawka(
                jednostki = Jednostki.GRAM,
                wartosc = 0f,
                kcal = 0f,
                bialko = 0f,
                weglowodany = 0f,
                tluszcze = 0f,
                blonnik = 0f
            ))
        ))
    )
    fun clearEditRecipe( ) {
        editRecipe.value = cleartedDanie.copy()
        recipeName.value =""
        indexRecipe.value = -1
    }

    var editRecipe = mutableStateOf<DaniaDetail>(cleartedDanie.copy())






}