package com.example.balansapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayout
import com.example.balansapp.ui.navigation.main.Screen
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.CalendarDialoge
import com.example.firstcomposeap.ui.components.getCurrentDate
import com.example.firstcomposeap.ui.components.getFormOnlyDate
import com.example.firstcomposeap.ui.components.icon.Edit_calendar
import com.example.firstcomposeap.ui.components.icon.Today
import com.example.firstcomposeap.ui.components.meal.WeeakDaysSelector
import com.example.firstcomposeap.ui.components.meal.FriendsMealTab
import com.example.firstcomposeap.ui.components.meal.UserMealTab
import com.example.firstcomposeap.ui.service.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealScreen(navController: NavHostController,
               loginViewModel: LoginViewModel,
               productViewModel: ProductViewModel
) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.menu_meal)) }

    var showSearchSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    productViewModel.token = loginViewModel.token

    val tabs = listOf(context.getString(R.string.menu_profil),
        context.getString(R.string.friends)
        )


    
    CalendarDialoge(
        baseDate = productViewModel.wybranaData,
        showDialog = showDatePicker,
        onDateSelected = { productViewModel.wybranaData = it },
        onDismiss = { showDatePicker = false },
        todayOnly = false
    )

    MainLayout(
        navController = navController,
        selectedItem = selectedItem,
        onItemSelected = { selectedItem = it }
    ) { innerPadding ->
        Column {
            RowDataInformation(
                baseDate = productViewModel.wybranaData,
                onToday = { productViewModel.wybranaData = getFormOnlyDate(getCurrentDate()) },
                onDataPicker = { showDatePicker = it },
            )
            WeeakDaysSelector(onClick = { productViewModel.wybranaData = it }, baseDate = productViewModel.wybranaData)


            Box(modifier = Modifier
                .padding(innerPadding),
                contentAlignment = Alignment.Center )
            {
                LogoBackGround()

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    TabRow(selectedTabIndex = productViewModel.selectedTabIndex) {
                        tabs.forEachIndexed { index, title ->
                            Tab(selected = productViewModel.selectedTabIndex == index,
                                onClick = {productViewModel.selectedTabIndex = index},
                                text =  {Text(title, fontSize = 22.sp)}
                                )

                        }
                    }

                    when (productViewModel.selectedTabIndex) {
                        0 -> UserMealTab(loginViewModel,
                            onAddClick = {showSearchSheet = true},
                            productViewModel = productViewModel,
                            date = productViewModel.wybranaData
                        )
                        1 -> FriendsMealTab(loginViewModel,
                            onAddClick = {showSearchSheet = true},
                            productViewModel = productViewModel,
                            date = productViewModel.wybranaData
                        )
                    }


                }
            }
        }
    }


    if (showSearchSheet) {
        navController.navigate(Screen.ProductSearch.route)
    }

}

@Composable 
fun RowDataInformation(baseDate: String,
                       onDataPicker: (Boolean) -> Unit,
                       onToday: (String) -> Unit
                       ) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            baseDate,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
        )

        IconButton(
            onClick = { onDataPicker(true) },
            modifier = Modifier
                .weight(1f)
                .shadow(10.dp, RectangleShape)
                .fillMaxHeight()
                .padding(5.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(10.dp)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Edit_calendar, contentDescription = "wybierz date", tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Wybierz datę", color = Color.White)
            }
        }

        IconButton(
            onClick = { onToday(baseDate) },
            modifier = Modifier
                .weight(1f)
                .shadow(10.dp, RectangleShape)
                .fillMaxHeight()
                .padding(5.dp)
                .background(color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(10.dp)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Today, contentDescription = "Dzisiejsza data", tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Dziś", color = Color.White)
            }
        }
    }
}