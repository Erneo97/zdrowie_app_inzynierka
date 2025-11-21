package com.example.firstcomposeap.ui.service

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.ApiClient
import com.example.firstcomposeap.ui.service.data.Produkt
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)


    var selectedProducts =   mutableStateListOf<Produkt>()


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