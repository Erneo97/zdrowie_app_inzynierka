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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayoutAdmin

@Composable
fun UserAdminScreen(navController: NavHostController ) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.users)) }

    val tabs = listOf(
        context.getString(R.string.users),
        "Zablokowani"
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
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LogoBackGround()
            }

            Column {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title, fontSize = 22.sp) }
                        )
                    }
                }

                Text("UserAdminScreen")
                when (selectedTabIndex) {
                    0 -> ActiveUsersTab()
                    1 -> BlockUsersTab()
                }
            }


        }
    }
}

@Composable
fun ActiveUsersTab() {
    Text("ActiveUsersTab", fontSize = 22.sp)
}

@Composable
fun BlockUsersTab() {
    Text("BlockUsersTab", fontSize = 22.sp)
}