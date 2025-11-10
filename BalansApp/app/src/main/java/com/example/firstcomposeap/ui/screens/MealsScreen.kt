package com.example.balansapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayout
import com.example.balansapp.ui.service.LoginViewModel
import kotlin.math.log

@Composable
fun MealScreen(navController: NavHostController, loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.menu_meal)) }
    var user = loginViewModel.user

    MainLayout(
        navController = navController,
        selectedItem = selectedItem,
        onItemSelected = { selectedItem = it }
    ) { innerPadding ->
        Column {
            WeeakDaysSelector()
            Box(modifier = Modifier
                .padding(innerPadding),
                contentAlignment = Alignment.Center )
            {
                LogoBackGround()


                Column (
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HeadText(
                        fontSize = 48.sp,
                        text = "To jest ekran posiłki"
                    )
                }
            }
        }
    }

}

@Composable
fun WeeakDaysSelector(dniNazwy: List<String> = listOf("Pn", "Wt", "Śr", "Czw", "Pt", "Sb", "Nd")) {
    var wybranyNumer by remember { mutableStateOf(0) }


    Text("Wybrany dzien ${dniNazwy.get(wybranyNumer)}")
    Row {
        dniNazwy.forEachIndexed { index, nazwa ->
            dayPoint(
                dayShortName = nazwa,
                dayNumber = index + 1,
                isSelected = index == wybranyNumer,
                onClick = { wybranyNumer = it }
            )
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
            .clickable { onClick(dayNumber - 1) }
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
