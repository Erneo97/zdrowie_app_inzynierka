package com.example.balansapp.ui.components.treningplans


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstcomposeap.ui.service.data.treningsPlanCard


@Composable
fun TrainingSeasonCard(
    treningPlan: treningsPlanCard,
    onClick: () -> Unit
) {
    var shadowColor = if (treningPlan.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    Box(
        modifier = Modifier
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = treningPlan.seasonName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Liczba treningów: ${treningPlan.trainingCount}", fontSize = 14.sp)

                Text(
                    text = "Obecnie używany: ${if (treningPlan.isActive) "Tak" else "Nie"}",
                    fontSize = 14.sp
                )

                Text(
                    text = "Cel treningu: ${treningPlan.goal.label}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${treningPlan.startDate} - ${treningPlan.endDate}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))

                FloatingActionButton(
                    onClick = onClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
