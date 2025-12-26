package com.example.balansapp.ui.screens.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.navigation.main.MainLayoutAdmin

@Composable
fun UserAdminScreen(navController: NavHostController ) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.users)) }

    MainLayoutAdmin (
        navController = navController,
        selectedItem = selectedItem,
        onItemSelected = { selectedItem = it }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("UserAdminScreen")
        }
    }
}