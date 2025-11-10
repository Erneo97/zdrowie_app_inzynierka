package com.example.firstcomposeap.ui.components.meal

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstcomposeap.ui.components.getCurrentDate
import com.example.firstcomposeap.ui.components.getFormOnlyDate
import com.example.firstcomposeap.ui.components.getWeekDayNumbers
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun WeeakDaysSelector(
    dniNazwy: List<String> = listOf("Pn", "Wt", "Śr", "Czw", "Pt", "Sb", "Nd"),
    onClick: (String) -> Unit,
    baseDate: String = getFormOnlyDate(getCurrentDate())
) {
    val date = LocalDate.parse(baseDate)
    val dayNumber = date.dayOfWeek.value

    var wybranyNumer by remember(baseDate) { mutableStateOf(dayNumber-1) }
    val mondayDate = date.minusDays((dayNumber - 1).toLong())

    val dniTygodnia = remember(baseDate) { getWeekDayNumbers(baseDate) }
//    Log.d("WeeakDaysSelector", "${dniTygodnia}")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            dniNazwy.forEachIndexed { index, nazwa ->
                dayPoint(
                    dayShortName = nazwa,
                    dayNumber = dniTygodnia[index],
                    isSelected = index == wybranyNumer ,
                    onClick = { clickedDay ->
                        wybranyNumer = dniTygodnia.indexOf(clickedDay)
                        val selectedDate = mondayDate.plusDays(index.toLong())
                        val formattedDate = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        onClick(formattedDate)
                    }
                )
            }
        }
    }
}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun dayPoint(
    dayShortName: String,
    dayNumber: Int,
    isSelected: Boolean,
    onClick: (Int) -> Unit
) {
    val colorSelected = MaterialTheme.colorScheme.primary
    val colorUnselected = Color.Transparent
    val colorTextSelected = Color.White
    val colorTextUnselected = MaterialTheme.colorScheme.onSurface

    val backgroundColor = if (isSelected) colorSelected else colorUnselected
    val textColor = if (isSelected) colorTextSelected else colorTextUnselected

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val rCircle = screenWidthDp * 0.1f
    val circlePadding = (screenWidthDp - 7 * rCircle/10 ) / 46

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(circlePadding.dp)
            .clickable { onClick(dayNumber) }
    ) {
        Box(
            modifier = Modifier
                .size(rCircle.dp)
                .background(backgroundColor, shape = CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayNumber.toString(),
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = dayShortName,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
