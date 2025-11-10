package com.example.firstcomposeap.ui.components.meal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstcomposeap.ui.components.icon.Delete
import com.example.firstcomposeap.ui.service.data.MealInfo

@Composable
fun MealProductAdded(meal: MealInfo, onClick : () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(2.dp).shadow(1.dp, RoundedCornerShape(4.dp)),
        verticalAlignment = Alignment.CenterVertically)
    {
        Box(Modifier.weight(9f)) {
            Row (Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(meal.nazwa, fontWeight = FontWeight.Bold, modifier = Modifier.weight(6f), fontSize = 25.sp)
                Text(text ="${ meal.dawka.wartosc} ${meal.dawka.jednostka.displayName}", fontSize = 15.sp, modifier = Modifier.weight(2f))
                Text(text ="${ meal.dawka.kcal} kcal", fontSize = 15.sp, modifier = Modifier.weight(2f))
            }
        }

        FloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(22.dp)
                .weight(1f)
        ) {
            Icon(
                imageVector = Delete,
                contentDescription = "Usuń element",
                tint = Color.White
            )
        }

    }
}