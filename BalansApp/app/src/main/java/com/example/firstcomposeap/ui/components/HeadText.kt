package com.example.balansapp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

@Composable
fun HeadText(
    text: String = "Balans",
    fontSize: TextUnit
) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        textAlign = TextAlign.Center,
        style = TextStyle(
            shadow = Shadow(
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.8f),
                offset = Offset(2f, 2f),
                blurRadius = 16f
            )
        )
    )
}