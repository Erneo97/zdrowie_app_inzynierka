package com.example.firstcomposeap.ui.components.statistic

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.firstcomposeap.ui.service.data.ChartPoint

@Composable
fun LineChartWithControls(
    points: List<ChartPoint>,
    xAxisLabel: String,
    yAxisLabel: String,
    a: Double? = null,
    b: Double? = null,
    modifier: Modifier = Modifier
) {
    var showGrid by remember { mutableStateOf(true) }
    var showTrendLine by remember { mutableStateOf(false) }

    Column(modifier = modifier.background(Color.White)) {
        LineChart(
            points = points,
            showGrid = showGrid,
            xAxisLabel = xAxisLabel,
            yAxisLabel = yAxisLabel,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            showTrendLine = showTrendLine,
            trendA = a,
            trendB =  b
        )
        Row {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = showGrid,
                    onCheckedChange = { showGrid = it }
                )
                Text("Pokaż siatkę")
            }


            if (a != null && b != null) {
                Spacer(Modifier.width(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = showTrendLine,
                        onCheckedChange = { showTrendLine = it }
                    )
                    Text("Pokaż linię trendu")
                }
            }
        }

    }
}


@SuppressLint("UseKtx")
@Composable
fun LineChart(
    points: List<ChartPoint>,
    showGrid: Boolean,
    xAxisLabel: String,
    yAxisLabel: String,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Blue,
    pointColor: Color = Color.Red,
    gridColor: Color = Color.LightGray,
    axisColor: Color = Color.Black,
    textColor: Color = Color.Black,
    strokeWidth: Double = 4.0,
    pointRadius: Double = 8.0,
    padding: Double = 64.0,
    showTrendLine: Boolean = false,
    trendA: Double? = null,
    trendB: Double? = null,
) {
    if (points.isEmpty()) return

    Canvas(modifier = modifier) {

        val minX = points.minOf { it.x }
        val maxX = points.maxOf { it.x }
        val minY = points.minOf { it.y }
        val maxY = points.maxOf { it.y }

        val rangeX = (maxX - minX).takeIf { it != 0.0 } ?: 1.0
        val rangeY = (maxY - minY).takeIf { it != 0.0 } ?: 1.0

        fun scaleX(x: Double): Float =
            (padding + (x - minX) / rangeX * (size.width - 2 * padding)).toFloat()

        fun scaleY(y: Double): Float =
            (size.height - padding - (y - minY) / rangeY * (size.height - 2 * padding)).toFloat()

        /* ================= GRID ================= */
        if (showGrid) {
            points.forEach { p ->
                val x = scaleX(p.x)
                val y = scaleY(p.y)

                drawLine(
                    color = gridColor,
                    start = Offset(x, padding.toFloat()),
                    end = Offset(x, (size.height - padding).toFloat()),
                    strokeWidth = 1f
                )

                drawLine(
                    color = gridColor,
                    start = Offset(padding.toFloat(), y),
                    end = Offset((size.width - padding).toFloat(), y),
                    strokeWidth = 1f
                )
            }
        }

        /* ================= AXES ================= */
        drawLine(
            color = axisColor,
            start = Offset(padding.toFloat(), padding.toFloat()),
            end = Offset(padding.toFloat(), (size.height - padding).toFloat()),
            strokeWidth = 3f
        )

        drawLine(
            color = axisColor,
            start = Offset(padding.toFloat(), (size.height - padding).toFloat()),
            end = Offset((size.width - padding).toFloat(), (size.height - padding).toFloat()),
            strokeWidth = 3f
        )

        /* ================= LINE ================= */
        val path = Path()
        points.sortedBy { it.x }.forEachIndexed { index, p ->
            val x = scaleX(p.x)
            val y = scaleY(p.y)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = strokeWidth.toFloat())
        )

        /* ================= POINTS ================= */
        points.forEach { p ->
            drawCircle(
                color = pointColor,
                radius = pointRadius.toFloat(),
                center = Offset(scaleX(p.x), scaleY(p.y))
            )
        }

        /* ================= TREND LINE ================= */
        if (showTrendLine && trendA != null && trendB != null) {

            val x1 = minX
            val x2 = maxX

            val y1 = trendA * x1 + trendB
            val y2 = trendA * x2 + trendB

            drawLine(
                color = Color.Magenta,
                start = Offset(scaleX(x1), scaleY(y1)),
                end = Offset(scaleX(x2), scaleY(y2)),
                strokeWidth = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 8f))
            )
        }

        /* ================= AXIS VALUES ================= */
        drawContext.canvas.nativeCanvas.apply {
            val paint = android.graphics.Paint().apply {
                color = textColor.toArgb()
                textSize = 28f
                textAlign = android.graphics.Paint.Align.CENTER
            }

            // X axis values
            points.forEach { p ->
                val x = scaleX(p.x)
                drawText(
                    "%.2f".format(p.x),
                    x,
                    size.height - padding.toFloat() + 32f,
                    paint
                )
            }

            // Y axis values
            paint.textAlign = android.graphics.Paint.Align.RIGHT
            points.forEach { p ->
                val y = scaleY(p.y)
                drawText(
                    "%.2f".format(p.y),
                    padding.toFloat() - 12f,
                    y + 8f,
                    paint
                )
            }

            /* ===== Axis labels ===== */
            paint.textAlign = android.graphics.Paint.Align.CENTER
            paint.textSize = 34f

            // X label
            drawText(
                xAxisLabel,
                size.width / 2,
                size.height - 8f,
                paint
            )

            // Y label
            save()
            rotate(-90f, 18f, size.height / 2)
            drawText(
                yAxisLabel,
                18f,
                size.height / 2,
                paint
            )
            restore()
        }
    }
}
