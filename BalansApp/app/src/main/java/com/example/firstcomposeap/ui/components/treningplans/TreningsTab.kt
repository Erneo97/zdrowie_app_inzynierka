package com.example.firstcomposeap.ui.components.treningplans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.firstcomposeap.ui.service.TreningViewModel

@Composable
fun TreningsTab (treningViewModel: TreningViewModel) {
    LaunchedEffect(Unit) {
        treningViewModel.downloadTreningsCard()
    }
//    StatisticPeriodSelector(
//        selectedPeriod = treningViewModel.selectedPeriod,
//        onSelected = {
//            treningViewModel.selectedPeriod = it
//            treningViewModel.days = it.days
//        }
//    )
    Column (modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())){
        treningViewModel.treningsCard.forEach {
            TrainingSeasonCard(
                trening = it,
                onClick = { } // TODO: dodać przekierowanie do okna statystyk treningu
            )
        }
    }


}