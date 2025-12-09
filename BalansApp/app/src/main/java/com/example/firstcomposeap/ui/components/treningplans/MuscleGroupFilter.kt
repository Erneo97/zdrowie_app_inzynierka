package com.example.firstcomposeap.ui.components.treningplans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.firstcomposeap.ui.service.data.GrupaMiesniowa
import kotlin.collections.forEach

/**
 *  Komponent obeujmuje wielokrotny wyór dowolnej liczby grup mięśniowych do wszyukiwania, wypisane w poziomie (scrollowalne),
 */
@Composable
fun MuscleGroupFilter(
    selected: List<GrupaMiesniowa>,
    onSelectedChange: (List<GrupaMiesniowa>, Boolean) -> Unit
) {
    var search by remember { mutableStateOf("") }
    var accurately by remember { mutableStateOf(false) }
    val allGroups = GrupaMiesniowa.entries.sortedBy {  it.grupaNazwa }
    val filtered = allGroups.filter {
        it.grupaNazwa.contains(search, ignoreCase = true)
    }

    Column  {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Szukaj grupy mięśniowej") },
                modifier = Modifier.weight(3f)
            )
            Column ( modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                Checkbox(
                    checked = accurately,
                    onCheckedChange = {accurately = it},
                )
                Spacer(Modifier.width(2.dp).height(3.dp))
                Text((if (accurately) "Dokładne" else "Zawierające"))
            }


        }


        Spacer(Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.heightIn(min = 0.dp, max = 100.dp)
                        .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selected.forEach { grupa ->
                FilterChip(
                    selected = true,
                    onClick = {
                        onSelectedChange(selected - grupa, accurately)
                    },
                    label = { Text(grupa.grupaNazwa) }
                )
            }
        }
        HorizontalDivider()
        Spacer(Modifier.height(4.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxHeight(0.1f),
            verticalArrangement = Arrangement.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            items(filtered) { grupa ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (grupa in selected) {
                                onSelectedChange(selected - grupa, accurately)
                            } else {
                                onSelectedChange(selected + grupa, accurately)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = grupa in selected,
                        onCheckedChange = {
                            if (it) onSelectedChange(selected + grupa, accurately)
                            else onSelectedChange(selected - grupa, accurately)
                        }
                    )
                    Text(
                        text = grupa.grupaNazwa,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }
        HorizontalDivider()
    }
}
