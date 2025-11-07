package com.example.balansapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.balansapp.R
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
                Text("To jest ekran posiłki", fontSize = 40.sp)
                Text("email: ${user?.email}", fontSize = 30.sp)
                Text("imie: ${user?.imie}", fontSize = 30.sp)
                Text("nazwisko: ${user?.nazwisko}", fontSize = 30.sp)
                Text("id: ${user?.id}", fontSize = 30.sp)
                Text("plec: ${user?.plec}", fontSize = 30.sp)
                Text("wzrost: ${user?.wzrost}", fontSize = 30.sp)
                Text("waga: ${user?.waga}", fontSize = 30.sp)
                Text("dania: ${user?.dania}", fontSize = 30.sp)
                Text("aktualnyPlan: ${user?.aktualnyPlan}", fontSize = 30.sp)
                Text("przyjaciele: ${user?.przyjaciele}", fontSize = 30.sp)

            }
        }

    }

}


