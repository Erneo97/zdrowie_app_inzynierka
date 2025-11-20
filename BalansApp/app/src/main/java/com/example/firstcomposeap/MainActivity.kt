package com.example.balansapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
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
import com.example.firstcomposeap.ui.screens.NewProductScreen
import com.example.firstcomposeap.ui.screens.ProductConsumedDetails
import com.example.firstcomposeap.ui.screens.SearchProductScreen
import com.example.firstcomposeap.ui.service.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loginViewModel: LoginViewModel = viewModel()
            val registerViewModel: RegisterViewModel = viewModel()
            val productViewModel: ProductViewModel = viewModel ()

            loginViewModel.login("michal@michal.michal", "michal")
            productViewModel.token = loginViewModel.token

            balansappTheme {
                val navController: NavHostController = rememberNavController()
                val context = LocalContext.current

                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    composable(Screen.Register.route) { RegisterScreen(navController, registerViewModel) }
                    composable(Screen.Login.route) { LoginScreen(navController, loginViewModel) }
                    composable(Screen.Home.route) { MealScreen(navController, loginViewModel) }
                    composable(Screen.Profile.route) { ProfileScreen(navController, loginViewModel) }
                    composable(Screen.TreningPlan.route ){ TreningsPlanScreen(navController) }
                    composable(Screen.Trenings.route) { TreningsScreen(navController) }

                    composable(Screen.NewProduct.route) { NewProductScreen(productViewModel, onClose = {navController.popBackStack()}) }
                    composable(Screen.ProductConsumedDetails.route) { ProductConsumedDetails(
                        productViewModel = productViewModel, onClose = {navController.popBackStack()}) }

                    composable(Screen.ProductSearch.route, ) { SearchProductScreen(
                        navController = navController,
                        onClose = {navController.popBackStack()},
                        productViewModel = productViewModel
                        ) }
                }
            }
        }
    }
}

