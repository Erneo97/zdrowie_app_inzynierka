package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavigationButtonsRetAdd(
                onClose = onClose,
                onAdd = {},
                mainText = "Zapisz zmiany"
            )
            Spacer(Modifier.height(10.dp))

            if( produkt != null ) {
                NutritionBox(
                    carbsLabel = "Węglowodany",
                    carbsValue = produkt.objetosc.get(0).weglowodany,
                    caloriesLabel = "Kalorie",
                    caloriesValue = produkt.objetosc.get(0).kcal,
                    fatLabel = "Tłuszcze",
                    fatValue = produkt.objetosc.get(0).tluszcze,
                    proteinLabel = "Białko",
                    proteinValue = produkt.objetosc.get(0).bialko,
                    modifier = Modifier.padding(16.dp)
                )

                Text(produkt.nazwa)
                Text(produkt.producent)
                Text("${produkt.objetosc.get(0).jednostki}")
            }
            else {
                CircularProgressIndicator()
            }



        }
    }
}

@Composable
fun NutritionBox(
    carbsLabel: String,
    carbsValue: Float,
    caloriesLabel: String,
    caloriesValue: Float,
    fatLabel: String,
    fatValue: Float,
    proteinLabel: String,
    proteinValue: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFFEFF7F5), shape = RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFFB7E5DD), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(carbsLabel, fontWeight = FontWeight.Bold)
                    Text("$carbsValue")
                }

                Divider(
                    color = Color(0xFFB7E5DD),
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(caloriesLabel, fontWeight = FontWeight.Bold)
                    Text("${caloriesValue}")
                }
            }

            Divider(
                color = Color(0xFFB7E5DD),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            )

            Row(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(fatLabel, fontWeight = FontWeight.Bold)
                    Text("${ fatValue }")
                }

                Divider(
                    color = Color(0xFFB7E5DD),
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(proteinLabel, fontWeight = FontWeight.Bold)
                    Text("${proteinValue}")
                }
            }
        }
    }
}
