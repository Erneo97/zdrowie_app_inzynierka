package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.firstcomposeap.ui.service.ProductViewModel

@Composable
fun ProductConsumedDetails(
    productViewModel: ProductViewModel,
    onClose: () -> Unit // TODO: zwracanie produktu poprawionego
) {
    var produkt = productViewModel.foundProduct

     Box(

        modifier = Modifier
            .padding(2.dp),
        contentAlignment = Alignment.Center  )
    {
        LogoBackGround()
        Column (
            modifier = Modifier
                .fillMaxSize()
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavigationButtonsRetAdd(
                onClose = onClose,
                onAdd = {},
                mainText = "Zapisz zmiany"
            )
            if( produkt != null ) {
                Text(produkt.nazwa)
                Text(produkt.producent)
                Text("${produkt.objetosc.get(0).jednostki}")
            }
            else {

                CircularProgressIndicator()
            }
            HeadText(
                fontSize = 48.sp,
                text = "To jest ekran ProductConsumedDetails"
            )


        }
    }

}