package com.example.balansapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayout
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.CalendarDialoge
import com.example.firstcomposeap.ui.components.getCurrentDate
import com.example.firstcomposeap.ui.components.getFormOnlyDate
import com.example.firstcomposeap.ui.components.icon.Edit_calendar
import com.example.firstcomposeap.ui.components.icon.Today
import com.example.firstcomposeap.ui.components.meal.WeeakDaysSelector

@Composable
fun MealScreen(navController: NavHostController, loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.menu_meal)) }
    var user = loginViewModel.user

    var wybranaData by remember { mutableStateOf(getFormOnlyDate(getCurrentDate())) }
    var showDatePicker by remember { mutableStateOf(false) }
    CalendarDialoge(
        baseDate = wybranaData,
        showDialog = showDatePicker,
        onDateSelected = { wybranaData = it },
        onDismiss = { showDatePicker = false },
        todayOnly = false
    )


    MainLayout(
        navController = navController,
        selectedItem = selectedItem,
        onItemSelected = { selectedItem = it }
    ) { innerPadding ->
        Column {
            Row{
                Text("Dziś: ${wybranaData}")
                Spacer(Modifier.width(58.dp))
                IconButton(
                    onClick = {showDatePicker = true},
                    modifier = Modifier.size(88.dp)) {
                    Row (verticalAlignment = Alignment.CenterVertically) {
                        Icon ( imageVector = Edit_calendar, contentDescription = "wybierz date")
                        Text("Wybierz datę")
                    }
                }
                Spacer(Modifier.width(50.dp))
                IconButton(
                    onClick = { wybranaData = getFormOnlyDate(getCurrentDate()) },
                    modifier = Modifier.size(68.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Today,
                            contentDescription = "Dzisiejsza data"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Dziś")
                    }
                }
            }

            WeeakDaysSelector(onClick = { wybranaData = it }, baseDate = wybranaData)
            Box(modifier = Modifier
                .padding(innerPadding),
                contentAlignment = Alignment.Center )
            {
                LogoBackGround()

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
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
