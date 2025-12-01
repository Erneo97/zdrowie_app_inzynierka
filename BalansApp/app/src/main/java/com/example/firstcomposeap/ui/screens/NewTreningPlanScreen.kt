package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.components.input.InputField
import com.example.firstcomposeap.ui.service.TreningViewModel


@Composable
fun NewTreningPlanScreen(treningViewModel: TreningViewModel, onCLose : () -> Unit ) {
    var nazwa by remember { mutableStateOf("") }

    var czyAktualny by remember { mutableStateOf(false) }
    var cel by remember { mutableStateOf("") }

    Spacer(Modifier.height(10.dp))


    Column(Modifier.fillMaxSize().padding(10.dp)) {
        NavigationButtonsRetAdd(
            onClose = { onCLose() },
            onAdd = { },
            mainText = "Zapisz nowy plan"
        )
        Spacer(Modifier.height(15.dp))


        Column(Modifier.weight(2f)
        ) {
            Text("Nazwa treningu: ")
            InputField(
                value = nazwa,
                onValueChange = { nazwa = it},
                label = "Nazwa treningu",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(15.dp))

            Text("Czy Chcesz by to był twoj aktualny plan?")
            Spacer(Modifier.height(5.dp))
            Row(Modifier.fillMaxWidth( ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = czyAktualny,
                    onCheckedChange = {czyAktualny = it},
                )
                Spacer(Modifier.width(4.dp).height(3.dp))
                Text((if (czyAktualny) "Aktualny" else "Dodatkowy") + " plan")
            }


        }




        // wyświetlanie dodanych ćwiczeń
        Spacer(Modifier.height(25.dp))
        Text("Dodaj Ćwiczenia", fontWeight = FontWeight.Bold, fontSize = 25.sp)
        FullSizeButton(
            text = "Dodaj ćwiczenie",
            onClick = {  onCLose()  },
        )
        Column (Modifier.weight(5f).verticalScroll(rememberScrollState())
        ) {

            Spacer(Modifier.height(10.dp))
            Text("Wybrane ćwiczenia:", fontWeight = FontWeight.Bold, fontSize = 25.sp)





        }

    }
    Spacer(Modifier.height(10.dp))
}