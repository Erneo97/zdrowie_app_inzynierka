package com.example.firstcomposeap.ui.components.treningplans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
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
    onSelectedChange: (List<GrupaMiesniowa>) -> Unit
) {
    var search by remember { mutableStateOf("") }

    val allGroups = GrupaMiesniowa.entries.sortedBy {  it.grupaNazwa }
    val filtered = allGroups.filter {
        it.grupaNazwa.contains(search, ignoreCase = true)
    }

    Column {
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Szukaj grupy mięśniowej") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selected.forEach { grupa ->
                FilterChip(
                    selected = true,
                    onClick = {
                        onSelectedChange(selected - grupa)
                    },
                    label = { Text(grupa.grupaNazwa) }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyRow {
            items(filtered) { grupa ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (grupa in selected) {
                                onSelectedChange(selected - grupa)
                            } else {
                                onSelectedChange(selected + grupa)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = grupa in selected,
                        onCheckedChange = {
                            if (it) onSelectedChange(selected + grupa)
                            else onSelectedChange(selected - grupa)
                        }
                    )
                    Text(
                        text = grupa.grupaNazwa,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
    }
}
