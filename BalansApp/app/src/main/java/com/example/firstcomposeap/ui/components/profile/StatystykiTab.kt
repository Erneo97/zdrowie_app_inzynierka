package com.example.firstcomposeap.ui.components.profile


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.UniversalEditCard
import com.example.firstcomposeap.ui.components.profile.StatystykiTab.PhysicalActivitySelector
import com.example.firstcomposeap.ui.components.icon.Question_mark
import com.example.firstcomposeap.ui.components.profile.StatystykiTab.ToolTipDialoge

//TODO: string.yml
@Composable
fun StatystykiTab (loginViewModel: LoginViewModel) {
    loginViewModel.calculatePPM()

    var showToolTip by remember { mutableStateOf(false)}
    var textToolTip by remember { mutableStateOf("") }


    UniversalEditCard(
        data = {
            Text("PPM: ${loginViewModel.ppm}", fontSize = 30.sp)
        },
        onClick =  {
            textToolTip = "Wzór Mifflina służy do obliczenia podstawowej przemiany materii - PPM. Podstawową przemianą materii (PPM) nazywamy najniższy poziom zapotrzebowania energetycznego umożliwiający zachowanie podstawowych funckji w optymalnych warunkach bytowych. Uzyskany wynik odnosi się jedynie do pracy układów i narządów, to wartość, poniżej której nie powinniśmy schodzić przy planowaniu diety. PPM najbardziej przydatne jest przy określaniu całkowitej przemiany materii"
            showToolTip = true},
        icon = Question_mark
    )



    var aktywnosc by remember { mutableStateOf<Double>(1.4) }
    var cpm by remember { mutableStateOf(loginViewModel.ppm * aktywnosc) }

    UniversalEditCard(
        data = {
            Column {
                Text("CPM: ${cpm}", fontSize = 30.sp)
                PhysicalActivitySelector (
                    selectedValue = aktywnosc,
                    onActivitySelected = {
                        aktywnosc = it
                         cpm = loginViewModel.ppm * it
                    }
                )
            }
        },
        onClick =  {
            textToolTip = "Całkowitą przemianę materii nazywamy sumę dobowych wydatków energetycznych związanych z metabolizmem podstawowym, niezbędnym do utrzymanaia podstawowych funkcji życiowych (PPM) i aktywnością fizyczną (współczynnikiem PAL z ang. physical activity level) " +
                    "\n1,4: siedzący tryb życia </b> (praca biurowa)" +
                    "1,5–1,6: niska aktywność (siedzący tryb pracy + 30-60 minut lekkiego wysiłku dziennie) \n" +
                    "1,7–1,8: umiarkowana aktywność (wysiłek o średnim nasileniu kilka razy w tygodniu) \n" +
                    "1,9–2,0: wysoka aktywność (codzienny wysiłek lub praca fizyczna) \n" +
                    "2,0–2,2: bardzo wysoka aktywność (codzienny, intensywny wysiłek przez ponad godzinę) ."
            showToolTip = true},
        icon = Question_mark
    )

    if( showToolTip ) {
        ToolTipDialoge (onConfirm = {showToolTip = false},
            text = textToolTip)
    }
}
