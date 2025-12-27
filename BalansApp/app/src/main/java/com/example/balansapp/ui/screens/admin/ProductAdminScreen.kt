package com.example.balansapp.ui.screens.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.navigation.main.MainLayoutAdmin
import com.example.balansapp.ui.navigation.main.Screen

@Composable
fun ProductAdminScreen(navController: NavHostController ) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.products)) }

    val tabs = listOf(
        "Prośby o zaakceptowanie produktu",
        "Zarządzaj produktami"
    )
    var selectedTabIndex by remember { mutableStateOf(0) }

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

            Column {
                FullSizeButton(
                    text = "Dodaj produkt",
                    onClick = { navController.navigate(Screen.NewProduct.route)},
                )

                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title, fontSize = 22.sp) }
                        )
                    }
                }


                when (selectedTabIndex) {
                    0 -> {}
                    1 -> {}
                }


            }
        }
    }
}