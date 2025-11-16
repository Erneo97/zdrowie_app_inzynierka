package com.example.firstcomposeap.ui.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstcomposeap.ui.service.data.Product
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    var token by mutableStateOf<String?>(null)

    


    fun addNewProduct(product: Product) {
        viewModelScope.launch {

        }
//         TODO:
    }
}