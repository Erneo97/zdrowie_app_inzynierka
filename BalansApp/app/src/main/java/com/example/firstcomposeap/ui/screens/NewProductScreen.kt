package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.balansapp.ui.components.FullSizeButton
import com.example.firstcomposeap.ui.service.ProductViewModel
import com.example.firstcomposeap.ui.service.data.Dawka
import com.example.firstcomposeap.ui.service.data.JEDNOSTKA
import java.nio.file.WatchEvent

@Composable
fun NewProductScreen(productViewModel: ProductViewModel,
                     onClose: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf(JEDNOSTKA.GRAM) }
    var unitList = remember { mutableStateListOf<Dawka>() }

    var dawka by remember {
        mutableStateOf(
            Dawka(
                jednostka = JEDNOSTKA.GRAM,
                wartosc = 100f,
                kcal = 230f,
                bialko = 10f,
                weglowodany = 30f,
                tluszcze = 5f,
                blonnik = 2f
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())){
        Text("NewProductScreen")
        Button(
            onClick = { onClose( )  }
        ) {  Text("Anuluj") }


        Text("Wybierz jednostkę:")


        DawkaForm(
            dawka = dawka,
            onDawkaChange = { dawka = it }
        )
        FullSizeButton("Dodaj jednostkę",
            onClick = {
                unitList.add(dawka)
            })

        Text("Dodane jednostki")
        unitList.forEachIndexed { index, dawka ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(2f).padding(5.dp)) {
                    Text("${index}")
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
                    Button(
                        onClick = { unitList.removeAt(index) },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Usuń")
                    }
                }
            }
        }

    }

}
@Composable
fun DawkaForm(
    dawka: Dawka,
    onDawkaChange: (Dawka) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    fun updateField(transform: (Dawka) -> Dawka) {
        onDawkaChange(transform(dawka))
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            NumericInput(
                label = "Ilość",
                value = dawka.wartosc,
                modifier = Modifier
                    .weight(2f)
                    .padding(end = 8.dp)
            ) { newValue ->
                updateField { it.copy(wartosc = newValue) }
            }

            Box(modifier = Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(dawka.jednostka.displayName)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    JEDNOSTKA.values().forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit.displayName) },
                            onClick = {
                                updateField { it.copy(jednostka = unit) }
                                expanded = false
                            }
                        )
                    }
                }
            }
        }


        NumericInput("Białko (g)", dawka.bialko) { newValue ->
            updateField { d -> d.copy(bialko = newValue) }
        }
        NumericInput("Węglowodany (g)", dawka.weglowodany) { newValue ->
            updateField { d -> d.copy(weglowodany = newValue) }
        }
        NumericInput("Tłuszcze (g)", dawka.tluszcze) { newValue ->
            updateField { d -> d.copy(tluszcze = newValue) }
        }
        NumericInput("Błonnik (g)", dawka.blonnik) { newValue ->
            updateField { d -> d.copy(blonnik = newValue) }
        }

    }
}

@Composable
fun NumericInput(
    label: String,
    value: Float,
    modifier: Modifier = Modifier,
    onChange: (Float) -> Unit
) {
    var text by remember { mutableStateOf(value.toString()) }

    OutlinedTextField(
        value = text,
        onValueChange = { input ->
            if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))) {
                text = input
                onChange(input.toFloatOrNull() ?: 0f)
            }
        },
        label = { Text(label) },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}
