package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.input.InputField
import com.example.firstcomposeap.ui.components.icon.Question_mark
import com.example.firstcomposeap.ui.service.TreningViewModel
import com.example.firstcomposeap.ui.service.data.GrupaMiesniowa


@Composable
fun NewExerciseScreen(treningViewModel: TreningViewModel, onCLose : () -> Unit ) {
    var nazwa by remember { mutableStateOf("") }
    var opis by remember { mutableStateOf("") }
    var spalanie by remember { mutableStateOf(0.0f) }
    var spalanieStr by remember { mutableStateOf(spalanie.toString()) }


    val grupyMiesniowe = GrupaMiesniowa.entries.toList().sortedBy { it.grupaNazwa }
    val selectedGrupyMiesniowe = remember { mutableStateListOf<GrupaMiesniowa>() }

    Column (Modifier
        .fillMaxSize()
        .padding(10.dp)){
        Spacer(Modifier.height(15.dp))

        Column(Modifier.weight(5f)) {
            NavigationButtonsRetAdd(
                onClose = { onCLose() },
                onAdd = { }, // TODO:
                mainText = "Zapisz nowe ćwiczenie"
            )
            Spacer(Modifier.height(20.dp))


            Text("Nazwa nowego planu trenigowego")
            InputField(
                value = nazwa,
                onValueChange = {nazwa = it},
                label = "Nazwa planu trenigowego",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))


            Text("Opis ćwiczenia")
            InputField(
                value = opis,
                onValueChange = {opis = it},
                label = "Nazwa planu trenigowego",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))

            Text("MET ćwiczenia")
            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InputField(
                    value = spalanieStr,
                    onValueChange = {
                        spalanieStr = it
                        if( spalanieStr != "")
                            spalanie = spalanieStr.toFloat()
                    },
                    label = "Nazwa planu trenigowego",
                    modifier = Modifier.weight(3f)
                )
                FloatingActionButton(
                    onClick = { }, //TODO : pokazywanie tooltip
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(0.5f).padding(5.dp).size(42.dp)
                ) {
                    Icon(
                        imageVector = Question_mark,
                        contentDescription = "co to MET",
                        tint = Color.White
                    )
                }

            }

        }


        // wybieranie przez checBox Grup mięśniowych jakie dane ćwiczenie obejmuje
        Spacer(Modifier.height(15.dp))
        Text("Wybierz grupy mięśniowe jakie ćwiczenie obejmuje", fontWeight = FontWeight.Bold, fontSize = 25.sp)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(4f)
                .padding(8.dp)
        ) {
            items(grupyMiesniowe) { grupa ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Checkbox(
                        checked = selectedGrupyMiesniowe.contains(grupa),
                        onCheckedChange = { checked -> selectMuscleGroup(checked, grupa, selectedGrupyMiesniowe)  }
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(grupa.grupaNazwa)
                }
            }
        }


        Spacer(Modifier.height(10.dp))
    }
}

// TODO: sprawdzić spójność przy wysyłaniu czy faktycznie są ogólne przy wybraniu szczegółowych

/**
 * Gdy wybierasz bardziej szczeułową grupę mięśniową jej ogólny odpowiednik też zostaje wybrany albo odznaczony, gdy już nie ma żadej z dokładenj podgrupy
 */
private fun selectMuscleGroup(checked: Boolean, grupa: GrupaMiesniowa, selectedGrupyMiesniowe: SnapshotStateList<GrupaMiesniowa>) {
    val nazaOgolna = grupa.grupaNazwa.split('-')[0].trim()
    if (checked) {
        selectedGrupyMiesniowe.add(grupa)
        val ret = GrupaMiesniowa.fromNazwa(nazaOgolna)
        if( ret != null && !selectedGrupyMiesniowe.contains(ret))
            selectedGrupyMiesniowe.add(ret)
    }
    else {
        selectedGrupyMiesniowe.remove(grupa)
        var count = 0
        selectedGrupyMiesniowe.forEach {
            if( it.grupaNazwa.split('-')[0].trim() == nazaOgolna)
                count++
        }

        val ret = GrupaMiesniowa.fromNazwa(nazaOgolna)
        if( ret != null && count == 1)
            selectedGrupyMiesniowe.remove(ret)
    }
}