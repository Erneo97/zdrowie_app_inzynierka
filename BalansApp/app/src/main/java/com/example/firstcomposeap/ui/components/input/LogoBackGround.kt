package com.example.balansapp.ui.components.input

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.balansapp.R

@Composable
fun LogoBackGround() {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "BackgroundLogo",
        modifier = Modifier
            .fillMaxWidth()
            .alpha(0.2f),
        contentScale = ContentScale.Crop
    )
}