package com.example.firstcomposeap.ui.components.meal

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstcomposeap.ui.components.icon.Keyboard_arrow_down
import com.example.firstcomposeap.ui.components.icon.Keyboard_arrow_up
import com.example.firstcomposeap.ui.service.data.MealInfo

@SuppressLint("UnrememberedMutableState", "DefaultLocale")
@Composable
fun TimeOfDayMealCard(title: String,
                      meals: SnapshotStateList<MealInfo>,
                      onAddClick: () -> Unit,
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
                    IconButton(onClick = { onAddClick()}, modifier = Modifier.background(MaterialTheme.colorScheme.primary,
                        CircleShape
                    )) {
                        Icon(Icons.Default.Add, contentDescription = "Dodaj", tint = Color.White)
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

private fun calculateCaloriesInMeal(meals: List<MealInfo>) : Double {
    var sum = 0.0

    meals.forEach { meal -> sum = sum + meal.objetosc.kcal }

    return sum
}