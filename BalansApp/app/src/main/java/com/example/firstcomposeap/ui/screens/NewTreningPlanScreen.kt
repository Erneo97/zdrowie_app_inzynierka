package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.FullSizeButton
import com.example.firstcomposeap.ui.service.TreningViewModel


@Composable
fun NewTreningPlanScreen(treningViewModel: TreningViewModel, onCLose : () -> Unit ) {
    Column {
        // wyświetlanie dodanych ćwiczeń
        Spacer(Modifier.height(25.dp))
        Text("Dodaj Ćwiczenia", fontWeight = FontWeight.Bold, fontSize = 25.sp)
        FullSizeButton(
            text = "Dodaj ćwiczenie",
            onClick = { onCLose() },
        )
        Column (Modifier.weight(3f).verticalScroll(rememberScrollState())
        ) {

            Spacer(Modifier.height(10.dp))
            Text("Wybrane ćwiczenia:", fontWeight = FontWeight.Bold, fontSize = 25.sp)





        }

    }
    Spacer(Modifier.height(10.dp))
}