package com.example.firstcomposeap.ui.components.meal

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.service.LoginViewModel

@Composable
fun friendsMealTab(loginViewModel: LoginViewModel,
                   date: String
) {
    HeadText(
        fontSize = 48.sp,
        text = "To jest ekran friendsMealTab"
    )
}