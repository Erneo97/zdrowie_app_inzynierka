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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.firstcomposeap.ui.service.ProductViewModel
import com.example.firstcomposeap.ui.service.data.Dawka
import com.example.firstcomposeap.ui.service.data.Produkt

/**
 * Komponent pokazujący szczegóły produktu.
 * Wybór jednostki jaką produkt sie posługuje, wartość.
 * Aktualizacja makro produktu w tabeli
 */
@Composable
fun ProductConsumedDetails(
    productViewModel: ProductViewModel,
    onClose: () -> Unit
) {
    val produkt = productViewModel.foundProduct ?: run {
        Box { CircularProgressIndicator() }
        return
    }


    var expanded by remember { mutableStateOf(false) }
    var userValuer by remember(produkt.id) {
        mutableStateOf(produkt.objetosc.first().wartosc)
    }


    val availableUnits by remember(produkt.id) {
        mutableStateOf(produkt.objetosc.map { it.jednostki }.distinct())
    }

    var selectedUnit by remember(produkt.id) {
        mutableStateOf(produkt.objetosc.first().jednostki)
    }

    val selectedDawka by remember(produkt.id, selectedUnit) {
        mutableStateOf(
            produkt.objetosc.firstOrNull { it.jednostki == selectedUnit }
                ?: produkt.objetosc.first()
        )
    }



    val factor = userValuer / selectedDawka.wartosc

    val currentFat = selectedDawka.tluszcze * factor
    val currentKcal = selectedDawka.kcal * factor
    val currentCarbs = selectedDawka.weglowodany * factor
    val currentProtein = selectedDawka.bialko * factor

    Box(

        modifier = Modifier
            .padding(2.dp),
        contentAlignment = Alignment.Center  )
    {
        LogoBackGround()
        Spacer(Modifier.height(15.dp))
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavigationButtonsRetAdd(
                onClose = {
                    productViewModel.foundProduct = null // wymusza załadowanie detali od w tym widoku
                    onClose()
                          },
                onAdd = {

                    val dawkaNowa = Dawka(
                        jednostki = selectedDawka.jednostki,
                        wartosc = userValuer,
                        kcal = currentKcal,
                        bialko = currentProtein,
                        weglowodany = currentCarbs,
                        tluszcze = currentFat,
                        blonnik = selectedDawka.blonnik * factor
                    )
                    productViewModel.consumedProduct =
                        Produkt(
                            id = produkt.id,
                            nazwa = produkt.nazwa,
                            producent = produkt.producent,
                            kodKreskowy = produkt.kodKreskowy,
                            objetosc = listOf(dawkaNowa)
                        )
                    onClose()
                    productViewModel.foundProduct = null // wymusza załadowanie detali od w tym widoku  po kliknięciu innego elementu
                },
                mainText = "Zapisz zmiany"
            )
            Spacer(Modifier.height(10.dp))

            if( produkt != null ) {
                Text("${produkt.producent} - ${produkt.nazwa}", fontWeight = FontWeight.Bold,
                    fontSize =  35.sp)
                Spacer(Modifier.height(10.dp))
                NutritionBox(
                    carbsLabel = "Węglowodany",
                    carbsValue = currentCarbs,
                    caloriesLabel = "Kalorie",
                    caloriesValue = currentKcal,
                    fatLabel = "Tłuszcze",
                    fatValue = currentFat,
                    proteinLabel = "Białko",
                    proteinValue = currentProtein,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NumericInput(
                        label = "Ilość",
                        value = userValuer,
                        onChange = {userValuer = it},
                        modifier = Modifier
                            .weight(2f)
                            .padding(end = 8.dp)
                    )

                    Box (modifier = Modifier.weight(1f)){
                        OutlinedButton(onClick = { expanded = true }) {
                            Text(selectedUnit.displayName)
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            availableUnits.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(unit.displayName) },
                                    onClick = {
                                        selectedUnit = unit
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }


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
                    Text("${carbsValue} g.")
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
                    Text("${caloriesValue} kcal")
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
                    Text("${ fatValue } g.")
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
                    Text("${proteinValue} g.")
                }
            }
        }
    }
}
