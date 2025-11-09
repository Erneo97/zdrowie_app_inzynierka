package com.example.firstcomposeap.ui.components.profile


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.UniversalEditCard
import com.example.firstcomposeap.ui.components.profile.StatystykiTab.Question_mark
import com.example.firstcomposeap.ui.components.profile.StatystykiTab.ToolTipDialoge

//TODO: string.yml
@Composable
fun StatystykiTab (loginViewModel: LoginViewModel) {
    HeadText(
        fontSize = 48.sp,
        text = "To jest ekran StatystykiTab"
    )
    loginViewModel.calculatePPM()

    var showToolTip by remember { mutableStateOf(false)}
    var textToolTip by remember { mutableStateOf("") }

    UniversalEditCard(
        data = {
            Text("PPM: ${loginViewModel.ppm}", fontSize = 30.sp)
        },
        onClick =  {
            textToolTip = "Wzór Mifflina służy do obliczenia podstawowej przemiany materii - PPM. Podstawową przemianą materii ( w skrócie PPM lub BMR z ang. basal metabolic rate) nazywamy najniższy poziom zapotrzebowania energetycznego umożliwiający zachowanie podstawowych funckji w optymalnych warunkach bytowych. Uzyskany wynik odnosi się jedynie do pracy układów i narządów, to wartość, poniżej której nie powinniśmy schodzić przy planowaniu diety. PPM najbardziej przydatne jest przy określaniu całkowitej przemiany materii"
            showToolTip = true},
        icon = Question_mark
    )

    if( showToolTip ) {
        ToolTipDialoge (onConfirm = {showToolTip = false},
            text = textToolTip)
    }

}