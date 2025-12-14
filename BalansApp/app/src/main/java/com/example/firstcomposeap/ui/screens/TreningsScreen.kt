package com.example.balansapp.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayout

@Composable
fun TreningsScreen(navController: NavHostController) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.menu_trenings) ) }

    val tabs = listOf(
        "Nowy trening",
        "Odbyte treningi"
    )
    var selectedTabIndex by remember { mutableStateOf(0) }

    MainLayout(
        navController = navController,
        selectedItem = selectedItem,
        onItemSelected = { selectedItem = it }
    ) {
            innerPadding -> Box(
                modifier = Modifier
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                LogoBackGround()

            Column (
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    0 -> newTreningTab()
                    1 -> TreningsTab()
                }

            }
        }
    }

}

@Composable
fun newTreningTab () {

}

@Composable
fun TreningsTab () {

}