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
import com.example.firstcomposeap.ui.service.data.MealGroup
import com.example.firstcomposeap.ui.service.data.PoraDnia
import com.example.firstcomposeap.ui.service.data.Produkt
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)


    val mealsMap = mutableStateMapOf<PoraDnia, MealGroup>(
        PoraDnia.SNIADANIE to MealGroup(PoraDnia.SNIADANIE, mutableStateListOf()),
        PoraDnia.LUNCH to MealGroup(PoraDnia.LUNCH, mutableStateListOf()),
        PoraDnia.OBIAD to MealGroup(PoraDnia.OBIAD, mutableStateListOf()),
        PoraDnia.KOLACJA to MealGroup(PoraDnia.KOLACJA, mutableStateListOf()),
        PoraDnia.PRZEKASKA to MealGroup(PoraDnia.PRZEKASKA, mutableStateListOf())
    )

    var selectedProducts =   mutableStateListOf<Produkt>() // wypełniany w widoku szukania produktów (SearchProductScreen za  pośrednictwem zmiennej consumeProduct) przekazywany do widoku posiłków w ciuągu dnia i tam czyszczony dane do dodania usuwanie przez przycisk w oknie
    var isSelectedProductsReadyToSend = mutableStateOf(false)
    var selectedDayTime = mutableStateOf(PoraDnia.CLEAR)

    var selectedRecipes =   mutableStateListOf<List<Produkt>>()
    var isSelectedRecipesReadyToSend = mutableStateOf(false)


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