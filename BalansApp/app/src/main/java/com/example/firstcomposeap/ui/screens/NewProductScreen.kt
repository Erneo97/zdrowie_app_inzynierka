package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.firstcomposeap.ui.service.ProductViewModel

@Composable
fun NewProductScreen(productViewModel: ProductViewModel,
                     onClose: () -> Unit
) {
    Column{
        Text("NewProductScreen")
        Button(
            onClick = { onClose( )  }
        ) {  Text("Anuluj") }
    }

}