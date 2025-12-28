package com.example.balansapp.ui.screens.admin

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayoutAdmin
import com.example.balansapp.ui.service.AdminVievModel
import com.example.balansapp.ui.service.data.UserCard

@Composable
fun UserAdminScreen(navController: NavHostController, adminVievModel: AdminVievModel ) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.users)) }

    LaunchedEffect(Unit) {
        adminVievModel.downloadUserList()
    }

    val tabs = listOf(
        context.getString(R.string.users),
        "Zablokowani"
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
                    0 -> UsersTab(itemVisibilityCodition = false, adminVievModel)
                    1 -> UsersTab(itemVisibilityCodition = true, adminVievModel)
                }
            }
        }
    }
}


@Composable
fun UsersTab(itemVisibilityCodition: Boolean, adminVievModel: AdminVievModel ) {
    if( adminVievModel.loadingData ) {
        CircularProgressIndicator()
    }
    else {
        var value by remember { mutableStateOf(1f) }

        Text("Dopuszczalna granica błędu : ${value.toInt()}", fontSize = 25.sp)
        Slider(
            value = value,
            onValueChange = { value = it },
            valueRange = 0f..25f
        )

        val filteredUsers = adminVievModel.usersList
            .filter { it.blocked == itemVisibilityCodition }

        LazyColumn {
            items(filteredUsers) { userItem ->
                UserItemCard(
                    userCard = userItem,
                    onBlockChange = {
                        userItem.blocked = it
                        adminVievModel.changeStatsUser(userItem.id, it)
                        adminVievModel.downloadUserList()
                    },
                    failureMaxCount = value.toInt()
                )
            }
        }
    }
}



@Composable
fun UserItemCard(userCard: UserCard,
                 onBlockChange: (Boolean) -> Unit,
                 failureMaxCount: Int = 100,
                 ) {
    var checked by remember {  mutableStateOf(userCard.blocked) }

    Box(
        modifier = getModiverCard(failureMaxCount >= userCard.failureCount)
    ) {
        Column {
            Text("${userCard.id}      ${userCard.imie} ${userCard.nazwisko}", fontSize = 25.sp)
            HorizontalDivider()
            Text("Email: ${userCard.email}")
            Text("Błędnie wprowadzone dane: ${userCard.failureCount}", fontWeight = FontWeight.Bold)

            Row (verticalAlignment = Alignment.CenterVertically){
                Text("Zablokować użytkownika: ")
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        onBlockChange(it)
                    }
                )
            }
        }


    }
}

@Composable
public fun getModiverCard(bool: Boolean ) : Modifier {
    val badColor = MaterialTheme.colorScheme.error
    val goodColor = MaterialTheme.colorScheme.primary
    var shadowColor = if (bool) goodColor else badColor

    LaunchedEffect(bool) {
        shadowColor = if (bool) goodColor else badColor
    }

    return Modifier
        .padding(8.dp)
        .drawBehind {
            val shadowOffsetX = 8f
            val shadowOffsetY = 8f
            val shadowColor = shadowColor.copy(alpha = 0.25f)
            drawRoundRect(
                color = shadowColor,
                topLeft = Offset(shadowOffsetX, shadowOffsetY),
                size = size,
                cornerRadius = CornerRadius(25f, 25f),
            )
        }
        .shadow(
            elevation = 10.dp,
            shape = RoundedCornerShape(25.dp),
            ambientColor = shadowColor.copy(alpha = 0.8f),
            spotColor = shadowColor.copy(alpha = 0.8f)
        )
        .border(
            width = 2.dp,
            color = Color.Gray.copy(alpha = 0.4f),
            shape = RoundedCornerShape(25.dp)
        )
        .background(Color.White, RoundedCornerShape(16.dp))
        .padding(8.dp)
}