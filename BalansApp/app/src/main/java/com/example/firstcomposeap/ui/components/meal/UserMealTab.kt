package com.example.firstcomposeap.ui.components.meal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.service.LoginViewModel

@Composable
fun userMealTab(loginViewModel: LoginViewModel,
                date: String
) {
    Spacer(Modifier.height(10.dp))
    if( loginViewModel.isLoadedUserData ) {
        loginViewModel.calculatePPM()
        CalorieProgressBar(loginViewModel.ppm, 2050.0, loginViewModel.user!!.zapotrzebowanieKcal.toDouble() )
    }
    else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }


    HeadText(
        fontSize = 48.sp,
        text = "To jest ekran userMealTab"
    )
}

