package com.example.firstcomposeap.ui.components.meal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CalorieProgressBar(
    minKcal: Double,
    consumedKcal: Double,
    maxKcal: Double,
    modifier: Modifier = Modifier
) {
    val progressFraction = consumedKcal.toFloat() / maxKcal.coerceAtLeast(1.0).toFloat()
    val barColor = if (consumedKcal <= maxKcal) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.error

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                .border(1.dp, MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(12.dp))
        ) {
            // Wypełnienie paska
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progressFraction.coerceIn(0f, 1f))
                    .background(barColor, shape = RoundedCornerShape(12.dp))
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Min: $minKcal kcal", style = MaterialTheme.typography.bodySmall)
            Text("Spożyte: $consumedKcal kcal", style = MaterialTheme.typography.bodySmall)
            Text("Max: $maxKcal kcal", style = MaterialTheme.typography.bodySmall)
        }
    }
}
