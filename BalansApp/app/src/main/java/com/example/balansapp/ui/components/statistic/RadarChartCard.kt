package com.example.balansapp.ui.components.statistic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.String
import kotlin.collections.List

@Composable
fun RadarChartCard (
    currentScope: List<RadarChartElement>,
    previousScope: List<RadarChartElement>? = null,
    textHead: String,
    sizeChart: Dp = 700.dp,
    radius: Float = 340f,
    textSize: Float = 60f,
    countCircle: Int = 5,
    heightCard: Dp = 550.dp,
    legentText: List<String>? = listOf("25.11.52", "24.11.42")
) {
    Box(
        modifier = Modifier
            .padding(6.dp)
            .shadow(elevation = 20.dp,
                shape = RoundedCornerShape(25.dp),
                clip = false)
            .background(Color.White)
            .border(
                width = 2.dp,
                color = Color.Gray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(25.dp)
            )
            .fillMaxWidth()
            .height(heightCard)
    ) {
        Column (
            modifier = Modifier
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = textHead,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            RadarChart(
                currentScope = currentScope,
                previousScope =previousScope,
                modifier = Modifier.fillMaxSize(),
                sizeChart = sizeChart,
                radius = radius,
                textSize = textSize,
                countCircle = countCircle,
                legentText = legentText
            )
        }

    }
}