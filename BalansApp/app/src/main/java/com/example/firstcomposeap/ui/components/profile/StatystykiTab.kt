package com.example.firstcomposeap.ui.components.profile


import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.UniversalEditCard
import com.example.firstcomposeap.ui.components.profile.StatystykiTab.PhysicalActivitySelector
import com.example.firstcomposeap.ui.components.icon.Question_mark
import com.example.firstcomposeap.ui.components.meal.SelectPomiarWagiOptionBox
import com.example.firstcomposeap.ui.components.profile.StatystykiTab.ToolTipDialoge
import com.example.firstcomposeap.ui.components.statistic.LineChartWithControls
import com.example.firstcomposeap.ui.components.statistic.StatisticPeriodSelector
import com.example.firstcomposeap.ui.components.statistic.StatisticsCard
import com.example.firstcomposeap.ui.service.StatisticViewModel
import com.example.firstcomposeap.ui.service.data.ChartPoint
import com.example.firstcomposeap.ui.service.data.PomiarWagiOptions
import com.example.firstcomposeap.ui.service.data.StatisticParameters
import kotlinx.coroutines.delay
import java.time.LocalDate


@Composable
fun StatystykiTab (loginViewModel: LoginViewModel, statisticViewModel: StatisticViewModel) {
    loginViewModel.calculatePPM()

    var showToolTip by remember { mutableStateOf(false)}
    var textToolTip by remember { mutableStateOf("") }

    var isRunning by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var elapsedMs by remember { mutableStateOf(0L) }

    var localDate by remember {  mutableStateOf<LocalDate>(LocalDate.now()) }


    var selectedValue by remember {  mutableStateOf<StatisticParameters?>(null) }
    var selectedLabel by remember {  mutableStateOf<String?>(null) }
    var points by remember { mutableStateOf<List<ChartPoint>>(emptyList()) }

    val scrollState = rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(0)
    }

    LaunchedEffect(Unit, statisticViewModel.token,  statisticViewModel.selectOption, statisticViewModel.days, localDate) {
        statisticViewModel.downloadUserStatistic(statisticViewModel.days, localDate)

        selectedLabel = "Statystyki ${statisticViewModel.selectOption.label}"
        selectedValue = statisticViewModel.getCorrectStatisticParameters(statisticViewModel.selectOption)
    }

    LaunchedEffect(statisticViewModel.loaded) {
        if( statisticViewModel.loaded) {
            selectedValue = statisticViewModel.getCorrectStatisticParameters(statisticViewModel.selectOption)
            points = statisticViewModel.getDataByOption(statisticViewModel.selectOption)
        }
    }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            startTime = SystemClock.uptimeMillis()
            Log.e("czas", "${formatMs(elapsedMs)} - ${elapsedMs}")
            while (isRunning) {
                elapsedMs = SystemClock.uptimeMillis() - startTime
                delay(100)
            }
        }
    }

    SelectPomiarWagiOptionBox(
        options = PomiarWagiOptions.entries,
        selectedOption = statisticViewModel.selectOption,
        onOptionSelected = {statisticViewModel.selectOption = it}
    )

    StatisticPeriodSelector(
        selectedPeriod = statisticViewModel.selectedPeriod,
        onSelected = {
            Log.e("downloadWeightsUserStatistic", "czas wybrany: ${it} - ${statisticViewModel.selectedPeriod}")
            statisticViewModel.selectedPeriod = it
            statisticViewModel.days = it.days
        }
    )

//     Poczatek treści statystyk

    Column (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState),

        ) {

        //**** PPM ****//

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


        //**** CPM ****//

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
                textToolTip = "Całkowitą przemianę materii nazywamy sumę dobowych wydatków energetycznych związanych z metabolizmem podstawowym, niezbędnym do utrzymanaia podstawowych funkcji życiowych (PPM) i aktywnością fizyczną (współczynnikiem PAL z ang. physical activity level) \n" +
                        "1,4: siedzący tryb życia (praca biurowa)\n" +
                        "1,5–1,6: niska aktywność (siedzący tryb pracy + 30-60 minut lekkiego wysiłku dziennie) \n" +
                        "1,7–1,8: umiarkowana aktywność (wysiłek o średnim nasileniu kilka razy w tygodniu) \n" +
                        "1,9–2,0: wysoka aktywność (codzienny wysiłek lub praca fizyczna) \n" +
                        "2,0–2,2: bardzo wysoka aktywność (codzienny, intensywny wysiłek przez ponad godzinę) ."
                showToolTip = true},
            icon = Question_mark
        )

        //**** Info statystyczne + Wykres ****//

        selectedValue?.let { value ->
            StatisticsCard(
                stats = value,
                label = selectedLabel ?: "Statystyki"
            )
            Spacer(Modifier.height(15.dp))

            LineChartWithControls(
                points = statisticViewModel.caloriesData,
                xAxisLabel = "Dni",
                yAxisLabel = "Kalorie",
                modifier = Modifier.padding(16.dp),
                a = statisticViewModel.caloriesStats?.a ?: 0.0,
                b = statisticViewModel.caloriesStats?.b ?: 0.0
            )

            LineChartWithControls(
                points = points,
                xAxisLabel = "Dni",
                yAxisLabel = "Waga",
                modifier = Modifier.padding(16.dp),
                a = value.a,
                b = value.b
            )
        }
    }


    if( showToolTip ) {
        ToolTipDialoge (onConfirm = {showToolTip = false},
            text = textToolTip)
    }
}




private fun formatMs(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val hundredths = (ms % 1000) / 10
    return "%02d:%02d.%02d".format(minutes, seconds, hundredths)
}






