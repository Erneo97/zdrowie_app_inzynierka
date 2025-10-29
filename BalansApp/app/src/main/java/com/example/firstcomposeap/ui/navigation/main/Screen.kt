package com.example.balansapp.ui.navigation.main

sealed class Screen(val route: String) {
    object Home : Screen("meal")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")
    object TreningPlan : Screen("treningsPlans")
    object Trenings : Screen("trenings")
}