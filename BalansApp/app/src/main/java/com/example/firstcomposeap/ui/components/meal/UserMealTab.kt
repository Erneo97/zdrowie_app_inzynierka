package com.example.firstcomposeap.ui.components.meal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.icon.Question_mark
import com.example.firstcomposeap.ui.components.profile.StatystykiTab.ToolTipDialoge
import com.example.firstcomposeap.ui.service.ProductViewModel
import com.example.firstcomposeap.ui.service.data.Dawka
import com.example.firstcomposeap.ui.service.data.Jednostki
import com.example.firstcomposeap.ui.service.data.MealInfo
import com.example.firstcomposeap.ui.service.data.PoraDnia

@Composable
fun userMealTab(loginViewModel: LoginViewModel,
                onAddClick: () -> Unit,
                productViewModel: ProductViewModel,
                date: String
) {

    var showToolTip by remember { mutableStateOf(false) }
    var textToolTip by remember { mutableStateOf("") }


    LaunchedEffect(productViewModel.isReadyToSend) {
        if( productViewModel.selectedDayTime.value != PoraDnia.CLEAR) {

//            TODO: wysyłamy zmiany

            productViewModel.selectedDayTime.value = PoraDnia.CLEAR
            productViewModel.isReadyToSend.value = false
        }
    }


    val mealbreakfast = remember { mutableStateListOf<MealInfo>()}
    val mealLunch = remember { mutableStateListOf<MealInfo>()}
    val mealDiner = remember { mutableStateListOf<MealInfo>()}
    val mealSupper = remember { mutableStateListOf<MealInfo>()}
    val mealAnother = remember { mutableStateListOf<MealInfo>()}

    val sampleMeals = remember { mutableStateListOf(
        MealInfo(
            id = 1,
            nazwa = "Owsianka z mlekiem",
            producent = "",
            kodKreskowy = "",
            objetosc = Dawka(
                jednostki = Jednostki .GRAM,
                wartosc = 250.0f,
                kcal = 320f,
                bialko = 12f,
                weglowodany = 45f,
                tluszcze = 8f,
                blonnik = 5f
            )
        ),
        MealInfo(
            id = 2,
            nazwa = "Jajecznica z 3 jaj",
            producent = "",
            kodKreskowy = "",
            objetosc = Dawka(
                jednostki = Jednostki .GRAM,
                wartosc = 180.0f,
                kcal = 280f,
                bialko = 18f,
                weglowodany = 2f,
                tluszcze = 22f,
                blonnik = 0f
            )
        ),
    ) }

    val mealsToday = listOf(  // lista wszystkich list z dzisiejszymi posilkami
        MealGroup(PoraDnia.SNIADANIE, mealbreakfast),
        MealGroup(PoraDnia.LUNCH, mealLunch),
        MealGroup(PoraDnia.OBIAD, mealDiner),
        MealGroup(PoraDnia.KOLACJA, mealSupper),
        MealGroup(PoraDnia.PRZEKASKA, mealAnother),
    )
    val mealsMap = mealsToday.associateBy { it.poraDnia }

    mealsMap[PoraDnia.LUNCH]!!.produkty.apply {
        clear()
        addAll(sampleMeals)
    }
    mealsMap[PoraDnia.KOLACJA]!!.produkty.apply {
        clear()
        addAll(sampleMeals)
    }
    mealsMap[PoraDnia.SNIADANIE]!!.produkty.apply {
        clear()
        addAll(sampleMeals)
        addAll(sampleMeals)
    }

    Spacer(Modifier.height(10.dp))
    if( loginViewModel.isLoadedUserData ) {
        loginViewModel.calculatePPM()
        Row (Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(modifier = Modifier
                .weight(9f)
                .padding(4.dp)) {
                CalorieProgressBar(loginViewModel.ppm, 2050.0, loginViewModel.user!!.zapotrzebowanieKcal.toDouble() )
            }

            FloatingActionButton(
                onClick = { showToolTip = true
                    textToolTip = "Przekroczenie zapotrzebowania kalorycznego zmienia kolor schematu na pomarańczowy.\n\n" +
                            "Nie musisz się obwiniać za przekroczenie kalorii, najważniejsze to by się nie poddawać i trzymać się kaloryczności w szerszej perspektywie."},
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(42.dp)
                    .weight(1f)
            ) {
                Icon(
                    imageVector = Question_mark,
                    contentDescription = "Pokaż tooltip",
                    tint = Color.White
                )
            }

        }



        val timeOfDays = PoraDnia.entries
            .filter { it != PoraDnia.CLEAR }
            .map { it.displayName }

        timeOfDays.forEach { timeOfDays ->
            TimeOfDayMealCard(title = timeOfDays,
                            meals = mealsMap[PoraDnia.fromDisplayName(timeOfDays)]!!.produkty.toList(),
                onAddClick = {
                    onAddClick()
                   productViewModel.selectedDayTime.value = PoraDnia.fromDisplayName(timeOfDays)
                             },
                onRemoveClick = {
                    meal -> sampleMeals.remove(meal)
//                    TODO: Zmiana wartosci na serwerze po usunieciu / aktualizacja
                }
                )

        }
    }
    else {
        // Dane się ładują
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }


    if( showToolTip ) {
        ToolTipDialoge (onConfirm = {showToolTip = false},
            text = textToolTip)
    }
}



data class MealGroup(
    val poraDnia: PoraDnia,
    val produkty: SnapshotStateList<MealInfo> = mutableStateListOf()
)