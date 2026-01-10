package com.example.firstcomposeap.ui.components.meal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/**
 * Pokazuje listę z etykietami plus wartośći.
 * Ma pasek gdzie skala (max wartość) jest to największa wartość z listy
 */
@Composable
fun MacroNutrientsDisplay(
    labels: List<String>,
    values: List<Float>,
    valuesRWS: List<Float>? = null,
    unit: String = "g",
    modifier: Modifier = Modifier
) {
    require(labels.size == values.size) { "Labels and values must have the same size" }

    Column(modifier = modifier.padding(8.dp)) {
        labels.forEachIndexed { index, label ->
            val value = values[index]

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${value.format(1)} ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                if( valuesRWS != null && valuesRWS.size > index ) {

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "/ ${valuesRWS[index].format(1)} (${(value * 100/valuesRWS[index]).format(2)}%)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(3.dp)
                    )
            ) {
                val maxVal = if( valuesRWS!= null && valuesRWS.size > index) valuesRWS[index] else value

                Box(
                    modifier = Modifier
                        .fillMaxWidth((value / maxVal).coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(3.dp))
                )
            }
        }
    }
}

private fun Float.format(digits: Int) = "%.${digits}f".format(this)