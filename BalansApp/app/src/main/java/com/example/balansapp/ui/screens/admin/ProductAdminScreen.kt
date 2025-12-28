package com.example.balansapp.ui.screens.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayoutAdmin
import com.example.balansapp.ui.navigation.main.Screen
import com.example.balansapp.ui.service.AdminVievModel
import com.example.firstcomposeap.ui.service.data.Produkt

@Composable
fun ProductAdminScreen(navController: NavHostController, adminVievModel: AdminVievModel ) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.products)) }

    LaunchedEffect(Unit) {
        adminVievModel.downloadProducToConfirmeList()
    }

    val tabs = listOf(
        "Prośby o zaakceptowanie produktu",
        "Zarządzaj produktami"
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
                FullSizeButton(
                    text = "Dodaj produkt",
                    onClick = { navController.navigate(Screen.NewProduct.route)},
                )

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
                    0 -> {
                        adminVievModel.productssList.forEach {
                            produkt -> produktToConfirm(
                            produkt = produkt,
                            onRemove = { },
                            onAccept = { }
                        )
                        }
                    }
                    1 -> {}
                }
            }
        }
    }
}

@Composable
fun produktToConfirm(produkt: Produkt,
                     onRemove: () -> Unit, 
                     onAccept: () -> Unit) {
    Column (
        modifier = getModiverCard(true)
    ) {
        Text("${produkt.producent} - ${produkt.nazwa}", fontWeight = FontWeight.Bold)
        HorizontalDivider()
        Text("Dostępne jednostki:")
        produkt.objetosc.forEach {
            Text("${it.wartosc} ${it.jednostki}")
        }

    }
}