package com.example.firstcomposeap.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.components.input.InputField
import com.example.firstcomposeap.ui.components.BlancCard
import com.example.firstcomposeap.ui.components.icon.Delete
import com.example.firstcomposeap.ui.service.ProductViewModel
import com.example.firstcomposeap.ui.service.data.Dawka
import com.example.firstcomposeap.ui.service.data.Jednostki
import com.example.firstcomposeap.ui.service.data.Produkt


@Composable
fun EditProductScreen(productViewModel: ProductViewModel,
                     onClose: () -> Unit
) {
    if( productViewModel.editedProduct == null ) {
        Box( modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
            ) {
            CircularProgressIndicator()
        }

    }
    else {
        val editProdukt = productViewModel.editedProduct

        val unitList = remember { mutableStateListOf<Dawka>().apply {
            addAll(editProdukt!!.objetosc)
        } }
        var nazwa by remember { mutableStateOf(editProdukt!!.nazwa) }
        var producent by remember { mutableStateOf(editProdukt!!.producent) }
        var kodkreskowy by remember { mutableStateOf("") }


        var dawka by remember {
            mutableStateOf(
                Dawka(
                    jednostki = Jednostki .LITR,
                    wartosc = 1f,
                    kcal = 0f,
                    bialko = 0f,
                    weglowodany = 0f,
                    tluszcze = 0f,
                    blonnik = 0f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){
            Spacer(modifier = Modifier.height(25.dp))
            Row {
                Button(modifier = Modifier.weight(1f),
                    onClick = { onClose( )
                    productViewModel.editedProduct = null }
                ) {  Text("Anuluj") }
                Button(modifier = Modifier.weight(3f),
                    onClick = {
                        productViewModel.updateProdukt(
                            Produkt(
                                nazwa = nazwa,
                                kodKreskowy = kodkreskowy,
                                producent = producent,
                                id = editProdukt!!.id,
                                objetosc = unitList,
                            )
                        )
                        onClose()
                        productViewModel.editedProduct = null
                    }
                ) {  Text("Edytuj produkt") }
            }


            BlancCard {
                Text("Wybierz jednostkę:")

                InputField(value = nazwa, onValueChange = {nazwa = it}, label = "Nazwa produktu" )
                Spacer(Modifier.height(8.dp))
                InputField(value = producent, onValueChange = {producent = it}, label = "Producent produktu" )
                Spacer(Modifier.height(8.dp))
                InputField(value = kodkreskowy, onValueChange = {kodkreskowy = it}, label = "Kod kreskowy" )

                Spacer(Modifier.height(20.dp))
                Text("Dodaj wartość odżywczą produktu", fontSize = 20.sp)
                Spacer(Modifier.height(20.dp))

                DawkaForm(
                    dawka = dawka,
                    onDawkaChange = { dawka = it }
                )
                FullSizeButton("Dodaj wartość",
                    onClick = {
                        unitList.add(dawka)
                    })
            }



            Text("Dodane jednostki", fontSize = 25.sp)
            Spacer(Modifier.height(11.dp))
            if(unitList.isEmpty() ) {
                Text("Nie dodano wartości odżywczych", fontSize = 20.sp)
            }
            else
                unitList.forEachIndexed { index, dawka ->
                    BlancCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier
                                .weight(2f)
                                .padding(5.dp)) {
                                Text("${index + 1}", fontSize = 30.sp)
                            }
                            Box(modifier = Modifier.weight(8f)) {
                                DawkaForm(
                                    dawka = dawka,
                                    onDawkaChange = { newDawka ->
                                        unitList[index] = newDawka
                                    },
                                )
                            }
                            Box(modifier = Modifier.weight(2f)) {
                                FloatingActionButton(
                                    onClick = {  unitList.removeAt(index)  },
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Delete,
                                        contentDescription = "Usuń",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }

                }
            Spacer(Modifier.height(25.dp))
        }
    }
}

