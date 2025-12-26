package com.example.balansapp.ui.navigation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.example.balansapp.R
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

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
        Column {
            Spacer(Modifier.height(20.dp))
            content(innerPadding)
        }
    }
}

@Composable
fun MainLayoutAdmin(
    navController: NavHostController,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        bottomBar = {
            BottomAdminMenu(
                selectedItem = selectedItem,
                onItemSelected = { item ->
                    onItemSelected(item)
                    when (item) {
                        context.getString(R.string.users) -> navController.navigate(Screen.UsersAdmin.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        context.getString(R.string.menu_profil) -> navController.navigate(Screen.ProfileAdmin.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        context.getString(R.string.products) -> navController.navigate(Screen.ProductAdmin.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        context.getString(R.string.exercises) -> navController.navigate(Screen.ExerciseAdmin.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column {
            Spacer(Modifier.height(20.dp))
            content(innerPadding)
        }
    }
}

