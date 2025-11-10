package com.example.balansapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayout
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.meal.WeeakDaysSelector

@Composable
fun MealScreen(navController: NavHostController, loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.menu_meal)) }
    var user = loginViewModel.user

    var wybranaData by remember { mutableStateOf("") }

    MainLayout(
        navController = navController,
        selectedItem = selectedItem,
        onItemSelected = { selectedItem = it }
    ) { innerPadding ->
        Column {
            Text("Dziś: ${wybranaData}")
            WeeakDaysSelector(onClick = { wybranaData = it })
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
