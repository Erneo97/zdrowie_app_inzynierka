package com.example.firstcomposeap.ui.components.statistic

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.firstcomposeap.ui.service.data.StatisticParameters
import com.example.firstcomposeap.ui.service.data.StatisticPeriod


@Composable
fun StatisticsCard(
    stats: StatisticParameters?,
    modifier: Modifier = Modifier,
    label: String = "Statystyki"
) {
    val shadowColor = MaterialTheme.colorScheme.primary
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .drawBehind {
            val shadowOffsetX = 8f
            val shadowOffsetY = 8f
            val shadowColor = shadowColor.copy(alpha = 0.25f)
            drawRoundRect(
                color = shadowColor,
                topLeft = Offset(shadowOffsetX, shadowOffsetY),
                size = size,
                cornerRadius = CornerRadius(25f, 25f),
            )
        }
        .shadow(
            elevation = 10.dp,
            shape = RoundedCornerShape(25.dp),
            ambientColor = shadowColor.copy(alpha = 0.8f),
            spotColor = shadowColor.copy(alpha = 0.8f)
        )
        .border(
            width = 2.dp,
            color = Color.Gray.copy(alpha = 0.4f),
            shape = RoundedCornerShape(25.dp)
        )
        .background(Color.White, RoundedCornerShape(16.dp))
        .padding(16.dp)
    ) {
        Column (modifier = Modifier.background(Color.White)){

            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            stats?.let {
                StatisticRow("Min", it.min)
                StatisticRow("Max", it.max)
                StatisticRow("Średnia", it.average)
                StatisticRow("Mediana", it.median)
            } ?: run {
                Text("Brak danych", color = Color.Gray)
            }
        }
    }
}

@Composable
fun StatisticPeriodSelector(
    selectedPeriod: StatisticPeriod,
    onSelected: (StatisticPeriod) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()
            ,horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            StatisticPeriod.entries.forEach { period ->
                FilterChip(
                    selected = period == selectedPeriod,
                    onClick = { onSelected(period) },
                    label = { Text(period.label) },
                    modifier = Modifier.weight(2f)

                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun StatisticRow(label: String, value: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(
            text = String.format("%.2f", value),
            fontWeight = FontWeight.Bold
        )
    }
}
