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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.icon.Question_mark
import com.example.firstcomposeap.ui.components.profile.StatystykiTab.ToolTipDialoge
import com.example.firstcomposeap.ui.service.data.Dawka
import com.example.firstcomposeap.ui.service.data.JEDNOSTKA
import com.example.firstcomposeap.ui.service.data.MealInfo

@Composable
fun userMealTab(loginViewModel: LoginViewModel,
                date: String
) {
    var showToolTip by remember { mutableStateOf(false) }
    var textToolTip by remember { mutableStateOf("") }


    Spacer(Modifier.height(10.dp))
    if( loginViewModel.isLoadedUserData ) {
        loginViewModel.calculatePPM()
        Row (Modifier.fillMaxWidth().fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(modifier = Modifier.weight(9f).padding(4.dp)) {
                CalorieProgressBar(loginViewModel.ppm, 2050.0, loginViewModel.user!!.zapotrzebowanieKcal.toDouble() )
            }

            FloatingActionButton(
                onClick = { showToolTip = true
                    textToolTip = "Przekroczenie zapotrzebowania kalorycznego zmienia kolor schematu na pomarańczowy.\n\n" +
                            "Nie musisz się obwiniać za przekroczenie kalorii, najważniejsze to by się nie poddawać i trzymać się kaloryczności w szerszej perspektywie."},
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(42.dp).weight(1f)
            ) {
                Icon(
                    imageVector = Question_mark,
                    contentDescription = "Pokaż tooltip",
                    tint = Color.White
                )
            }

        }



        val sampleMeals =remember { mutableStateListOf(
            MealInfo(
                id = 1,
                nazwa = "Owsianka z mlekiem",
                dawka = Dawka(
                    jednostka = JEDNOSTKA.GRAM,
                    wartosc = 250.0,
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
                dawka = Dawka(
                    jednostka = JEDNOSTKA.GRAM,
                    wartosc = 180.0,
                    kcal = 280f,
                    bialko = 18f,
                    weglowodany = 2f,
                    tluszcze = 22f,
                    blonnik = 0f
                )
            ),
            MealInfo(
                id = 3,
                nazwa = "Kurczak z ryżem i warzywami",
                dawka = Dawka(
                    jednostka = JEDNOSTKA.GRAM,
                    wartosc = 350.0,
                    kcal = 450f,
                    bialko = 35f,
                    weglowodany = 40f,
                    tluszcze = 12f,
                    blonnik = 6f
                )
            ),
            MealInfo(
                id = 4,
                nazwa = "Kanapka z szynką i serem",
                dawka = Dawka(
                    jednostka = JEDNOSTKA.GRAM,
                    wartosc = 180.0,
                    kcal = 330f,
                    bialko = 17f,
                    weglowodany = 30f,
                    tluszcze = 15f,
                    blonnik = 2f
                )
            ),
            MealInfo(
                id = 5,
                nazwa = "Sałatka z tuńczykiem",
                dawka = Dawka(
                    jednostka = JEDNOSTKA.GRAM,
                    wartosc = 250.0,
                    kcal = 260f,
                    bialko = 25f,
                    weglowodany = 8f,
                    tluszcze = 14f,
                    blonnik = 4f
                )
            ),
            MealInfo(
                id = 6,
                nazwa = "Zupa pomidorowa z ryżem",
                dawka = Dawka(
                    jednostka = JEDNOSTKA.MILILITR,
                    wartosc = 400.0,
                    kcal = 180f,
                    bialko = 6f,
                    weglowodany = 30f,
                    tluszcze = 4f,
                    blonnik = 3f
                )
            ),
            MealInfo(
                id = 7,
                nazwa = "Jogurt naturalny 2%",
                dawka = Dawka(
                    jednostka = JEDNOSTKA.GRAM,
                    wartosc = 150.0,
                    kcal = 90f,
                    bialko = 5f,
                    weglowodany = 6f,
                    tluszcze = 4f,
                    blonnik = 0f
                )
            ),
            MealInfo(
                id = 8,
                nazwa = "Banan",
                dawka = Dawka(
                    jednostka = JEDNOSTKA.GRAM,
                    wartosc = 120.0,
                    kcal = 105f,
                    bialko = 1f,
                    weglowodany = 27f,
                    tluszcze = 0.3f,
                    blonnik = 3f
                )
            ),
            MealInfo(
                id = 9,
                nazwa = "Makaron z sosem bolońskim",
                dawka = Dawka(
                    jednostka = JEDNOSTKA.GRAM,
                    wartosc = 400.0,
                    kcal = 520f,
                    bialko = 28f,
                    weglowodany = 60f,
                    tluszcze = 18f,
                    blonnik = 5f
                )
            ),
            MealInfo(
                id = 10,
                nazwa = "Omlet z warzywami",
                dawka = Dawka(
                    jednostka = JEDNOSTKA.GRAM,
                    wartosc = 200.0,
                    kcal = 250f,
                    bialko = 16f,
                    weglowodany = 6f,
                    tluszcze = 18f,
                    blonnik = 2f
                )
            )
        ) }

        val timeOfDays = listOf( "Śniadanie", "Lunch", "Obiad", "Kolacja", "Przekąska")

        timeOfDays.forEach { timeOfDays ->
            TimeOfDayMealCard(title = timeOfDays,
                            meals = sampleMeals,
                onAddClick = {},
                onRemoveClick = {  meal -> sampleMeals.remove(meal)}
                )

        }

//        sampleMeals.forEach { mealInfo ->
//            MealProductAdded(mealInfo, onClick = {sampleMeals.remove(mealInfo)} )
//
//        }


    }
    else {
        // Dane śię ładują
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

