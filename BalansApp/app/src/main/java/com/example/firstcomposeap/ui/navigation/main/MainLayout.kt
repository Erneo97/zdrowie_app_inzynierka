package com.example.balansapp.ui.navigation.main

import androidx.compose.foundation.layout.PaddingValues
import com.example.balansapp.R
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
@Composable
fun MainLayout(
    navController: NavHostController,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        bottomBar = {
            BottomMenu(
                selectedItem = selectedItem,
                onItemSelected = { item ->
                    onItemSelected(item)
                    when (item) {
                        context.getString(R.string.menu_meal) -> navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        context.getString(R.string.menu_profil) -> navController.navigate(Screen.Profile.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        context.getString(R.string.menu_trenings) -> navController.navigate(Screen.Trenings.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        context.getString(R.string.menu_plans) -> navController.navigate(Screen.TreningPlan.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}
