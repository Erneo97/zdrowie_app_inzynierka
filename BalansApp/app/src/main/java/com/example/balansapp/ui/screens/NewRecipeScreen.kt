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
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.firstcomposeap.ui.service.data.calculateCaloriesInMeal
import com.example.firstcomposeap.ui.service.data.isSameProduct
import com.example.firstcomposeap.ui.service.data.toMealInfo


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
    val context = LocalContext.current
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
                onClose = { onClose()
                    productViewModel.selectedTabIndexProductRecipe = 1
                          },
                onAdd = {
                    if( productViewModel.recipeName.value != "" && productViewModel.selectedProductsFromRecipe.size > 0)
                    {
                        productViewModel.selectedTabIndexProductRecipe = 1
                        if( productViewModel.indexRecipe.value == -1) // nowy przepis
                            loginViewModel.addNewRecipe(productViewModel.selectedProductsFromRecipe, productViewModel.recipeName.value)
                        else { // edycja istniejącego przepisu
                            loginViewModel.updateNewRecipe(productViewModel.indexRecipe.value,
                                productViewModel.selectedProductsFromRecipe,
                                productViewModel.recipeName.value
                            )
                        }
                        onClose()
                    }
                    else {
                        Toast.makeText(context, "Należy dodać nazwę i produkty by zapisać danie", Toast.LENGTH_LONG).show()
                    }

                },
                mainText = "Zapisz posiłek"
            )
        }

        Spacer(Modifier.height(25.dp))
        showMaroRecipe(productViewModel)




        Spacer(Modifier.height(20.dp))
        Text("Podaj nazwę posiłku:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        InputField(value = productViewModel.recipeName.value,
            onValueChange = {productViewModel.recipeName.value = it},
            modifier = Modifier.fillMaxWidth(),
            label = "Podaj nazwę dania"
        )
        Spacer(Modifier.height(10.dp))

       FullSizeButton(
            text = "Dodaj produkty",
            onClick = {
                productViewModel.selectedTabIndexProductRecipe = 0
                goToSearchProduct()
            }
        )

        Spacer(Modifier.height(25.dp))
        Text("Wybrane produkty:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))


        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(productViewModel.selectedProductsFromRecipe) { produkt ->
                MealProductAdded(
                    meal = produkt.toMealInfo(),
                    onClick = {
                        productViewModel.selectedProductsFromRecipe.removeIf { isSameProduct(it, produkt) }
                    }
                )
            }
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
                      meals: List<MealInfo>,
                      onEditClick: () -> Unit,
                    isChecked: Boolean = false,
                    onCheckedChange: (List<MealInfo>, Boolean) -> Unit,

) {
    var expanded by remember { mutableStateOf(false) }

    val countMeal by derivedStateOf { meals.count() }
    val sumCalories by derivedStateOf {calculateCaloriesInMeal(meals)}

    var innerisChecked by remember {   mutableStateOf(isChecked) }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(6.dp))
            .padding(15.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
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
                    fontSize = 20.sp,
                    modifier = Modifier.weight(3f)
                )
                Text("${String.format("%.0f", sumCalories)}kcal  (${countMeal})",
                    modifier = Modifier.weight(2f))

                Row (modifier = Modifier.weight(3f)) {
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

                    Checkbox(
                        checked = innerisChecked,
                        onCheckedChange = { checked ->
                            innerisChecked = !innerisChecked
                            onCheckedChange(meals, innerisChecked)

                        }
                    )

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
                                onClick = {  }, // powinno zostać puste, nie będzie przycisku do zmiany listy
                                visiblityButton = false
                            )
                        }
                    }
                }
            }
        }
    }
}

