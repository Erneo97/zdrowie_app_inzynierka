package com.example.balansapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.balansapp.ui.screens.LoginScreen
import com.example.balansapp.ui.screens.RegisterScreen
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.balansapp.ui.navigation.main.Screen
import com.example.balansapp.ui.screens.MealScreen
import com.example.balansapp.ui.screens.ProfileScreen
import com.example.balansapp.ui.screens.TreningsPlanScreen
import com.example.balansapp.ui.screens.TreningsScreen
import com.example.balansapp.ui.service.LoginViewModel
import com.example.balansapp.ui.service.RegisterViewModel
import com.example.balansapp.ui.theme.balansappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loginViewModel: LoginViewModel = viewModel()
            val registerViewModel: RegisterViewModel = viewModel()
//            loginViewModel.login("michal@michal.michal", "michal")
            balansappTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route
                ) {
                    composable(Screen.Register.route) { RegisterScreen(navController, registerViewModel) }
                    composable(Screen.Login.route) { LoginScreen(navController, loginViewModel) }
                    composable(Screen.Home.route) { MealScreen(navController, loginViewModel) }
                    composable(Screen.Profile.route) { ProfileScreen(navController) }
                    composable(Screen.TreningPlan.route ){ TreningsPlanScreen(navController) }
                    composable(Screen.Trenings.route) { TreningsScreen(navController) }
                }
            }
        }
    }
}

