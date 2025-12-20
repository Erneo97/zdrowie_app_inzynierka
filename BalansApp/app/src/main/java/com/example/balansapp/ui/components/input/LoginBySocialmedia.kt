package com.example.balansapp.ui.components.input

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun LoginBySocialmedia (
    id: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Image(painter = painterResource(id = id),
        contentDescription = contentDescription,
        modifier = Modifier.size(60.dp).shadow(20.dp, RoundedCornerShape(12.dp))
            .clickable {
                onClick()
            })
}