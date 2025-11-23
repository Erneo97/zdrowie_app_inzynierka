package com.example.firstcomposeap.ui.components.meal

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.icon.Question_mark
import com.example.firstcomposeap.ui.components.profile.StatystykiTab.ToolTipDialoge
import com.example.firstcomposeap.ui.service.ProductViewModel
import com.example.firstcomposeap.ui.service.data.PoraDnia
import com.example.firstcomposeap.ui.service.data.toMealInfoList

@Composable
fun UniversalMealTab(loginViewModel: LoginViewModel,
                onAddClick: () -> Unit,
                productViewModel: ProductViewModel,
                date: String,
                downloadMealUserDay: suspend () -> Unit,
                     updataMealUser: () -> Unit

) {

    var showToolTip by remember { mutableStateOf(false) }
    var textToolTip by remember { mutableStateOf("") }

    val mealsMap = productViewModel.mealsMap

    LaunchedEffect(productViewModel.isSelectedRecipesReadyToSend) { // wysyłanie na serwer nowego przepisu

    }
//    Log.e("UniversalMealTab", "${productViewModel.mealsMap[PoraDnia.SNIADANIE]!!.produkty.size} - ${productViewModel.mealsMap[PoraDnia.LUNCH]!!.produkty.size} - ${productViewModel.mealsMap[PoraDnia.OBIAD]!!.produkty.size} - ${productViewModel.mealsMap[PoraDnia.KOLACJA]!!.produkty.size}")

    LaunchedEffect(productViewModel.isSelectedProductsReadyToSend) { // wysyłanie do bazy danych użytkownika posiłku
        if( productViewModel.selectedDayTime.value != PoraDnia.CLEAR) {
            mealsMap[productViewModel.selectedDayTime.value]!!.produkty.addAll(
                productViewModel.selectedProducts.toMealInfoList()
            )
            productViewModel.selectedProducts.clear()
            updataMealUser()

            productViewModel.selectedDayTime.value = PoraDnia.CLEAR
            productViewModel.isSelectedProductsReadyToSend.value = false
            productViewModel.calculateAllStatistic()
        }
    }


    LaunchedEffect(date, productViewModel.token) {  // pobieranie danych na serwer przy zmianie daty
        if (!productViewModel.token.isNullOrBlank()) {
            downloadMealUserDay()
        }
    }


    Spacer(Modifier.height(10.dp))
    if( loginViewModel.isLoadedUserData && productViewModel.isLoadedMeals) {
        loginViewModel.calculatePPM()
        Row (Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(modifier = Modifier
                .weight(9f)
                .padding(4.dp)) {
                CalorieProgressBar(loginViewModel.ppm,
                    productViewModel.consumedCalloriesThisDay,
                    loginViewModel.user!!.zapotrzebowanieKcal.toDouble() )
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
                meals = mealsMap[PoraDnia.fromDisplayName(timeOfDays)]!!.produkty,
                onAddClick = {
                    onAddClick()
                    productViewModel.selectedDayTime.value = PoraDnia.fromDisplayName(timeOfDays)
                },
                onRemoveClick = {meal ->
                    productViewModel.mealsMap[PoraDnia.fromDisplayName(timeOfDays)]!!.produkty.remove(meal)
//                    TODO: Zmiana wartosci na serwerze po  aktualizacja
                    productViewModel.selectedDayTime.value = PoraDnia.fromDisplayName(timeOfDays)
                    updataMealUser()
                    productViewModel.selectedDayTime.value = PoraDnia.CLEAR
                    productViewModel.calculateAllStatistic()
                }
            )

        }

        val labels = listOf("Białko", "Węglowodany", "Tłuszcze", "Błonnik")
        val values = listOf(productViewModel.makroMeal.value.bialko,
            productViewModel.makroMeal.value.weglowodany,
            productViewModel.makroMeal.value.tluszcze,
            productViewModel.makroMeal.value.blonnik)

        Text("Makro spożyte: ", fontWeight = FontWeight.SemiBold)
        MacroNutrientsDisplay(
            labels = labels,
            values = values,
            unit = "g",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(15.dp))
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

