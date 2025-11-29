package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.meal.MealProductAdded
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.components.input.InputField
import com.example.firstcomposeap.ui.components.icon.Keyboard_arrow_down
import com.example.firstcomposeap.ui.components.icon.Keyboard_arrow_up
import com.example.firstcomposeap.ui.components.meal.MacroNutrientsDisplay
import com.example.firstcomposeap.ui.service.ProductViewModel
import com.example.firstcomposeap.ui.service.data.Dawka
import com.example.firstcomposeap.ui.service.data.Jednostki
import com.example.firstcomposeap.ui.service.data.MealInfo
import com.example.firstcomposeap.ui.service.data.PoraDnia
import com.example.firstcomposeap.ui.service.data.calculateCaloriesInMeal


/**
 * Ekran dodawania nowego przepisu (Dania) do konta użytkownika, jego prywatne przepisy dostepne tylko dla niego.
 * Pozwala szybko uzupełniać maro danego dnia
 */
@Composable
fun NewRecipeScreen(loginViewModel: LoginViewModel,
                    productViewModel: ProductViewModel,
                    onClose: () -> Unit,
                    goToSearchProduct: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            NavigationButtonsRetAdd(
                onClose = { onClose() },
                onAdd = {
//                    TODO()
                },
                mainText = "Zapisz posiłek"
            )
        }

        Spacer(Modifier.height(25.dp))
        showMaroRecipe(productViewModel)


        var recipeName by remember { mutableStateOf("") }

        Spacer(Modifier.height(20.dp))
        Text("Podaj nazwę posiłku:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        InputField(value = recipeName,
            onValueChange = {recipeName = it},
            modifier = Modifier.fillMaxWidth(),
            label = "Podaj nazwę dania"
        )
        Spacer(Modifier.height(10.dp))

       FullSizeButton(
            text = "Dodaj produkty",
            onClick = { goToSearchProduct() }
        )

        Spacer(Modifier.height(25.dp))
        Text("Wybrane produkty:", fontSize = 20.sp, fontWeight = FontWeight.Bold)

//        TODO: lista wybranych produktów
        for( produkt  in productViewModel.selectedProductsFromRecipe) {
                Text("${produkt.nazwa} - ${produkt.objetosc}")
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun showMaroRecipe(productViewModel: ProductViewModel ) {
    var data by remember { mutableStateOf(Dawka(
        jednostki = Jednostki.SZTUKI,
        wartosc = 0.0f,
        kcal = 0.0f,
        bialko = 0.0f,
        weglowodany = 0.0f,
        tluszcze = 0.0f,
        blonnik = 0.0f
    )) }
    LaunchedEffect(productViewModel.selectedProductsFromRecipe.size) {
        Log.e("showMaroRecipe", "${data.kcal} - ${data.bialko} -${productViewModel.selectedProductsFromRecipe.size}")
        var newBialko = 0f
        var newWegl = 0f
        var newTluszcz = 0f
        var newBlonnik = 0f
        var newKcal = 0f

        productViewModel.selectedProductsFromRecipe.forEach { meal ->
            val m = meal.objetosc[0]
            newBialko += m.bialko
            newWegl += m.weglowodany
            newTluszcz += m.tluszcze
            newBlonnik += m.blonnik
            newKcal += m.kcal
        }

        data = data.copy(
            bialko = newBialko,
            weglowodany = newWegl,
            tluszcze = newTluszcz,
            blonnik = newBlonnik,
            kcal = newKcal
        )
    }

    val labels = listOf("Białko", "Węglowodany", "Tłuszcze", "Błonnik")
    val values = listOf(data.bialko, data.weglowodany, data.tluszcze, data.blonnik)


    Text("Makro w posiłłku: ", fontWeight = FontWeight.SemiBold)
    MacroNutrientsDisplay(
        labels = listOf("Kcal"),
        values = listOf(data.kcal),
        unit = "kcal",
        modifier = Modifier.fillMaxWidth()
    )

    MacroNutrientsDisplay(
        labels = labels,
        values = values,
        unit = "g",
        modifier = Modifier.fillMaxWidth()
    )
}


/**
 * Karta pokazująca dany posiłek. Jego kcal, nazwę, można rozwinąć i podejrzeć zawartość
 */
@SuppressLint("UnrememberedMutableState", "DefaultLocale")
@Composable
fun RecipeCard(title: String,
                      meals: SnapshotStateList<MealInfo>,
                      onEditClick: () -> Unit,
               // TODO: Wybranie tego posilku
                      onRemoveClick: (MealInfo) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val countMeal by derivedStateOf { meals.count() }
    val sumCalories by derivedStateOf {calculateCaloriesInMeal(meals)}


    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth().shadow(3.dp, RoundedCornerShape(6.dp))
            .padding(8.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            //  Nagłówek
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
                Text("${String.format("%.0f", sumCalories)}kcal  (${countMeal})")

                Row {
                    IconButton(onClick = { onEditClick()}, modifier = Modifier.background(MaterialTheme.colorScheme.primary,
                        CircleShape
                    )) {
                        Icon(Icons.Default.Edit, contentDescription = "Edytuj", tint = Color.White)
                    }
                    IconButton(onClick = { expanded = !expanded },
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)) {
                        Icon(
                            imageVector = if (expanded)
                                Keyboard_arrow_up
                            else
                                Keyboard_arrow_down,
                            contentDescription = if (expanded) "Zwiń" else "Rozwiń"
                            , tint = if (!expanded)  Color.White else MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            //  Zawartość rozwijana
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    if (meals.isEmpty()) {
                        Text(
                            text = "Brak posiłków",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        meals.forEach { meal ->
                            MealProductAdded(
                                meal = meal,
                                onClick = { onRemoveClick(meal) }
                            )
                        }
                    }
                }
            }
        }
    }
}

