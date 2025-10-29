package com.example.balansapp.ui.components.statistic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class RadarChartElement(
    val name: String,
    val value: Float,
    val color: Color
)

@Composable
fun RadarChart(
    currentScope: List<RadarChartElement>,
    previousScope: List<RadarChartElement>? = null ,
    modifier: Modifier = Modifier,
    sizeChart: Dp = 700.dp,
    radius: Float = 350f,
    textSize: Float = 60f,
    countCircle: Int = 5,
    legentText: List<String>? = listOf("Aktualne", "Poprzednie")
) {

    val sides = currentScope.size
    Column(
        modifier = modifier.wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {

            legentText?.getOrNull(0)?.let { LegendItem(color = Color.Green, label = it)}
            if (previousScope != null) {
                Spacer(modifier = Modifier.width(24.dp))
                legentText?.getOrNull(1)?.let { LegendItem(color = Color.Red, label = it)}
            }
        }
        Canvas(modifier = modifier.size(sizeChart)) {
            val center = Offset(size.width / 2, size.height / 2)

            drawRadarGrid(center, radius, sides, countCircle)
            drawRadarAxes(center, radius, sides, currentScope, textSize)


            if( previousScope != null ) {
                drawRadarChart(center, radius, previousScope, Color.Red)
            }
            drawRadarChart(center, radius, currentScope, Color.Green)
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .padding(end = 6.dp)
                .then(Modifier)
                .let { Modifier },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(18.dp)) {
                drawCircle(color = color)
            }
        }
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black)
    }
}
private fun DrawScope.drawRadarGrid(
    center: Offset,
    radius: Float,
    sides: Int,
    countCircle: Int
) {

    for (i in 1..countCircle) {
        val currentRadius = radius * i / countCircle
        drawCircle(
            color = Color.Black,
            radius = currentRadius,
            center = center,
            style = Stroke(width = 2f)
        )
    }

    for (i in 0 until sides) {
        val angle = 2f * PI.toFloat() * i / sides - PI.toFloat() / 2
        val endX = center.x + radius * cos(angle)
        val endY = center.y + radius * sin(angle)

        drawLine(
            color = Color.Black,
            start = center,
            end = Offset(endX, endY),
            strokeWidth = 1f
        )
    }
}

private fun DrawScope.drawRadarAxes(
    center: Offset,
    radius: Float,
    sides: Int,
    currentScope: List<RadarChartElement>,
    textSizeDefault: Float
) {
    for (i in currentScope.indices) {
        val angle = 2f * PI.toFloat() * i / sides - PI.toFloat() / 2
        val labelRadius = radius + 100f

        val labelX = center.x + labelRadius * cos(angle)
        val labelY = center.y + labelRadius * sin(angle)

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                currentScope[i].name,
                labelX,
                labelY,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = textSizeDefault
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )

            drawText(
                "%.2f".format(currentScope[i].value),
                labelX,
                labelY + textSizeDefault + 10f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = textSizeDefault * 0.8f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}


private fun DrawScope.drawRadarChart(
    center: Offset,
    radius: Float,
    currentScope: List<RadarChartElement>,
    colorDraw: Color
) {
    val sides = currentScope.size
    val path = Path()

    val maxValue = currentScope.maxOf { it.value }

    for (i in currentScope.indices) {
        val angle = 2f * PI.toFloat() * i / sides - PI.toFloat() / 2
        val valueRadius = radius * currentScope[i].value / maxValue
        val x = center.x + valueRadius * cos(angle)
        val y = center.y + valueRadius * sin(angle)

        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }

    path.close()

    drawPath(
        path = path,
        color = colorDraw.copy(alpha = 0.3f),
        style = Fill
    )

    drawPath(
        path = path,
        color = colorDraw,
        style = Stroke(width = 3f)
    )


    for (i in currentScope.indices) {
        val angle = 2f * PI.toFloat() * i / sides - PI.toFloat() / 2
        val valueRadius = radius * currentScope[i].value / maxValue
        val x = center.x + valueRadius * cos(angle)
        val y = center.y + valueRadius * sin(angle)

        drawCircle(
            color = currentScope[i].color,
            radius = 16f,
            center = Offset(x, y)
        )

    }
}
