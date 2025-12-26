package com.example.balansapp.ui.screens.user


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayout
import com.example.balansapp.ui.navigation.main.Screen
import com.example.balansapp.ui.service.LoginViewModel
import com.example.firstcomposeap.ui.components.treningplans.NewTreningTab
import com.example.firstcomposeap.ui.components.treningplans.TreningsTab
import com.example.firstcomposeap.ui.notification.saveLastTreningAction
import com.example.firstcomposeap.ui.service.TreningViewModel
import kotlinx.coroutines.launch

@Composable
fun TreningsScreen(navController: NavHostController, treningViewModel: TreningViewModel, loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.menu_trenings) ) }

    if( loginViewModel.user != null)
        treningViewModel.userWeight = loginViewModel.user!!.waga.last().wartosc.toFloat()

    val tabs = listOf(
        "Nowy trening",
        "Odbyte treningi"
    )
    val scope = rememberCoroutineScope()

    MainLayout(
        navController = navController,
        selectedItem = selectedItem,
        onItemSelected = { selectedItem = it }
    ) {
            innerPadding -> Box(
                modifier = Modifier
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                LogoBackGround()

            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if( treningViewModel.trening == null ) {
                    FullSizeButton(
                        text = "Rozpocznij Trening",
                        onClick = { treningViewModel.getAcctualTrening()
                            scope.launch {
                                saveLastTreningAction(context = context)
                            }
                        }
                    )
                }
                else {
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { treningViewModel.trening = null },
                            modifier = Modifier.weight(2f)
                        ) {
                            Text("Anuluj", fontSize = 15.sp)

                        }

                        Button(
                            onClick = {
                                treningViewModel.updateTrening()
                                      },
                            modifier = Modifier.weight(3f)
                        ) {
                            Text("Zapisz trening", fontSize = 15.sp)
                        }
                    }
                }

                TabRow(selectedTabIndex = treningViewModel.selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = treningViewModel.selectedTabIndex == index,
                            onClick = { treningViewModel.selectedTabIndex = index },
                            text = { Text(title, fontSize = 22.sp) }
                        )
                    }
                }

                when (treningViewModel.selectedTabIndex) {
                    0 -> NewTreningTab(treningViewModel = treningViewModel)
                    1 -> TreningsTab(treningViewModel = treningViewModel, onTreningStats = {navController.navigate(
                        Screen.TreningStats.route)})
                }

            }
        }
    }

}



