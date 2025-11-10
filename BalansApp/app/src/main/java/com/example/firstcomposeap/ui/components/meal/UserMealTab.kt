package com.example.firstcomposeap.ui.components.meal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.icon.Question_mark
import com.example.firstcomposeap.ui.components.profile.StatystykiTab.ToolTipDialoge

@Composable
fun userMealTab(loginViewModel: LoginViewModel,
                date: String
) {
    var showToolTip by remember { mutableStateOf(false) }
    var textToolTip by remember { mutableStateOf("") }


    Spacer(Modifier.height(10.dp))
    if( loginViewModel.isLoadedUserData ) {
        loginViewModel.calculatePPM()
        Row (Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(modifier = Modifier.weight(9f).padding(4.dp)) {
                CalorieProgressBar(loginViewModel.ppm, 2050.0, loginViewModel.user!!.zapotrzebowanieKcal.toDouble() )
            }

            FloatingActionButton(
                onClick = { showToolTip = true
                    textToolTip = "Przekroczenie zapotrzebowania kalorycznego zmienia kolor schematu na pomarańczowy.\n\n" +
                            "Nie musisz się obwiniać za przekroczenie kalorii, najważniejsze to by się nie poddawać i trzymać się kaloryczności w szerszej perspektywie."},
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(42.dp).weight(1f)
            ) {
                Icon(
                    imageVector = Question_mark,
                    contentDescription = "Pokaz tooltip",
                    tint = Color.White
                )
            }

        }

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


    if( showToolTip ) {
        ToolTipDialoge (onConfirm = {showToolTip = false},
            text = textToolTip)
    }
}

