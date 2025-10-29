package com.example.balansapp.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.navigation.main.MainLayout

@Composable
fun TreningsPlanScreen(navController: NavHostController) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.menu_plans)) }

    MainLayout(
        navController = navController,
        selectedItem = selectedItem,
        onItemSelected = { selectedItem = it }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("To jest ekran Plany treningowe ", fontSize = 40.sp)
        }
    }


}
