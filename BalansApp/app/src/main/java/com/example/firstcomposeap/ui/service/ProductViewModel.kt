package com.example.firstcomposeap.ui.service

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balansapp.ui.service.ApiClient
import com.example.firstcomposeap.ui.service.data.Product
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var message by mutableStateOf<String?>(null)



    fun addNewProduct(product: Product) {
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
}