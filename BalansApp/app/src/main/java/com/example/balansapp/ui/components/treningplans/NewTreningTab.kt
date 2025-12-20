package com.example.firstcomposeap.ui.components.treningplans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstcomposeap.ui.service.TreningViewModel
import kotlinx.coroutines.delay


@Composable
fun NewTreningTab (treningViewModel: TreningViewModel) {
    LaunchedEffect(treningViewModel.trening) {

        while (treningViewModel.trening != null ) {
            delay(1000L)
            var sum = 0.0f
            treningViewModel.trening!!.cwiczenia.forEach {
                sum += it.met * treningViewModel.userWeight * parseTimeToHours(it.czas)
            }
            treningViewModel.spaloneKcal = sum
        }
    }

    if( treningViewModel.trening == null ) {
        Text("Rozpocznij trening by kontynułować")
    }
    else {
        Text("${treningViewModel.trening!!.data}, spalone kcal: ${String.format("%.2f", treningViewModel.spaloneKcal)} kcal",
            fontWeight = FontWeight.SemiBold, fontSize = 25.sp
        )
        Spacer(Modifier.height(5.dp))
        HorizontalDivider()

        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            treningViewModel.trening!!.cwiczenia.forEach {
                CwiczenieTreningItem(
                    cwiczenie = it,
                    onRemove = { treningViewModel.trening!!.cwiczenia.remove(it) },
                    updateTime = {
                            nowyCzas -> it.czas = nowyCzas
                    }
                )
            }
        }
    }
}
