package com.example.balansapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.example.balansapp.ui.screens.LoginScreen
import com.example.balansapp.ui.screens.RegisterScreen
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.balansapp.ui.navigation.main.Screen
import com.example.balansapp.ui.screens.user.MealScreen
import com.example.balansapp.ui.screens.user.ProfileScreen
import com.example.balansapp.ui.screens.TestScreen
import com.example.balansapp.ui.screens.user.TreningsPlanScreen
import com.example.balansapp.ui.screens.user.TreningsScreen
import com.example.balansapp.ui.service.LoginViewModel
import com.example.balansapp.ui.service.RegisterViewModel
import com.example.balansapp.ui.theme.balansappTheme
import com.example.firstcomposeap.ui.screens.NewExerciseScreen
import com.example.firstcomposeap.ui.screens.NewProductScreen
import com.example.firstcomposeap.ui.screens.NewRecipeScreen
import com.example.firstcomposeap.ui.screens.NewTreningPlanScreen
import com.example.firstcomposeap.ui.screens.ProductConsumedDetails
import com.example.firstcomposeap.ui.screens.SearchExerciseScreen
import com.example.firstcomposeap.ui.screens.SearchProductScreen
import com.example.firstcomposeap.ui.screens.TreningStatsScreen
import com.example.firstcomposeap.ui.service.ProductViewModel
import com.example.firstcomposeap.ui.service.StatisticViewModel
import com.example.firstcomposeap.ui.service.TreningViewModel
import android.Manifest
import com.example.balansapp.ui.screens.admin.ExerciseAdminScreen
import com.example.balansapp.ui.screens.admin.ProductAdminScreen
import com.example.balansapp.ui.screens.admin.ProfileAdminScreen
import com.example.balansapp.ui.screens.admin.UserAdminScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        setContent {
            val loginViewModel: LoginViewModel = viewModel()
            val registerViewModel: RegisterViewModel = viewModel()
            val statisticViewModel : StatisticViewModel = viewModel ()
            val productViewModel: ProductViewModel = viewModel ()
            val treningViewModel : TreningViewModel = viewModel ()

            loginViewModel.login("michal@michal.michal", "michal")

            statisticViewModel.token = loginViewModel.token
            productViewModel.token = loginViewModel.token
            treningViewModel.token = loginViewModel.token



            balansappTheme {
                val navController: NavHostController = rememberNavController()

                NavHost(
                    navController = navController,

                    startDestination = if ( (loginViewModel.user?.role ?: "USER") == "ADMIN" ) Screen.UsersAdmin.route  else Screen.Home.route
                ) {
                    composable(Screen.Register.route) { RegisterScreen(navController, registerViewModel) }
                    composable(Screen.Login.route) { LoginScreen(navController, loginViewModel) }
                    composable(Screen.Home.route) { MealScreen(navController, loginViewModel, productViewModel) }
                    composable(Screen.Profile.route) { ProfileScreen(navController, loginViewModel, statisticViewModel) }
                    composable(Screen.TreningPlan.route ){ TreningsPlanScreen(navController, treningViewModel) }
                    composable(Screen.Trenings.route) { TreningsScreen(navController,
                        treningViewModel, loginViewModel) }

                    composable(Screen.TreningStats.route) {
                        TreningStatsScreen(treningViewModel = treningViewModel
                            , onClose = {
                                navController.popBackStack()
                                treningViewModel.clearTreningScreen()
                            } )
                    }

                    composable(Screen.Test.route) { TestScreen(statisticViewModel) }

                    composable(Screen.NewExercise.route) {
                        NewExerciseScreen(treningViewModel,
                            onCLose = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.NewTreningPlan.route) {
                        NewTreningPlanScreen(treningViewModel,
                            onCLose = { navController.popBackStack() },
                            onExerciseScrean = { navController.navigate(Screen.ExerciseSearch.route) }
                        )
                    }

                    composable(Screen.ExerciseSearch.route) { SearchExerciseScreen(
                        onClose = { navController.popBackStack()},
                        treningViewModel = treningViewModel,
                        loginViewModel = loginViewModel
                    ) }

                    composable(Screen.NewProduct.route) { NewProductScreen(productViewModel, onClose = {navController.popBackStack()}) }
                    composable(Screen.ProductConsumedDetails.route) { ProductConsumedDetails(
                        productViewModel = productViewModel, onClose = {navController.popBackStack()}) }

                    composable(Screen.NewRecipe.route) {
                        NewRecipeScreen(
                            loginViewModel = loginViewModel,
                            productViewModel = productViewModel,
                            onClose = {navController.popBackStack()
                                productViewModel.clearCreateProduct()
                            },
                            goToSearchProduct = {
                                navController.navigate(Screen.ProductSearch.createRoute(onlyProduct = true))
                            },
                        )
                    }

                    composable(route = Screen.ProductSearch.route,
                        arguments = listOf(
                            navArgument("onlyProduct") {
                                type = NavType.BoolType
                                defaultValue = false
                            }
                        )
                    ) {backStackEntry ->
                        val onlyProduct = backStackEntry.arguments?.getBoolean("onlyProduct") ?: false
                        SearchProductScreen(
                        navController = navController,
                        onClose = {navController.popBackStack()},
                        productViewModel = productViewModel,
                            loginViewModel = loginViewModel,
                        onlyProduct = onlyProduct
                        ) }


//****************************      WIDOKI ADMINA *************************************************
                    composable(Screen.ProfileAdmin.route) { ProfileAdminScreen( navController, loginViewModel)}
                    composable(Screen.UsersAdmin.route) { UserAdminScreen(navController = navController) }
                    composable(Screen.ExerciseAdmin.route) { ExerciseAdminScreen(navController = navController) }
                    composable(Screen.ProductAdmin.route) { ProductAdminScreen(navController = navController) }
                }
            }
        }
    }
}

