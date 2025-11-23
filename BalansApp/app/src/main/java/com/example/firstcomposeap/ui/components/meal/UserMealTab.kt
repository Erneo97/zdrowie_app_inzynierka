package com.example.firstcomposeap.ui.components.meal

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.service.ProductViewModel



@SuppressLint("UnrememberedMutableState")
@Composable
fun UserMealTab(loginViewModel: LoginViewModel,
                onAddClick: () -> Unit,
                productViewModel: ProductViewModel,
                date: String
) {
    UniversalMealTab(
        loginViewModel = loginViewModel,
        onAddClick = onAddClick,
        productViewModel = productViewModel,
        date = date,
        downloadMealUserDay = {productViewModel.downloadMealUserDay()},
        updataMealUser = {productViewModel.updateUserMeal()}
    )
}



