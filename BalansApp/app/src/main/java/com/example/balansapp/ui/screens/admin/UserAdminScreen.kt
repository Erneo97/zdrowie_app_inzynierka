package com.example.balansapp.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.balansapp.R
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.navigation.main.MainLayoutAdmin
import com.example.balansapp.ui.service.data.Plec
import com.example.balansapp.ui.service.data.UserCard

@Composable
fun UserAdminScreen(navController: NavHostController ) {
    val context = LocalContext.current
    var selectedItem by remember { mutableStateOf(context.getString(R.string.users)) }

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
                    0 -> UsersTab(itemVisibilityCodition = false)
                    1 -> UsersTab(itemVisibilityCodition = true)
                }
            }


        }
    }
}


@Composable
fun UsersTab(itemVisibilityCodition: Boolean ) {
    var value by remember { mutableStateOf(4f) }


    Text("Dopuszczalna granica błędu : ${value.toInt()}", fontSize = 25.sp)
    Slider(
        value = value,
        onValueChange = { value = it },
        valueRange = 0f..25f
    )



    val user = UserCard(
        id = 47,
        imie = "Michał",
        nazwisko = "Kaniewski",
        email = "michalkaniewski1997@gmail.com",
        plec = Plec.MEZCZYZNA,
        role = "ADMIN",
        failureCount = 8,
        blocked = false
    )

    val user2 = UserCard(
        id = 48,
        imie = "Michał",
        nazwisko = "Kaniewski",
        email = "michalkaniewski1997@gmail.com",
        plec = Plec.MEZCZYZNA,
        role = "USER",
        failureCount = 9,
        blocked = true
    )
    val usersList = mutableListOf<UserCard>()
    usersList.add(user2)
    usersList.add(user)
    usersList.add(user)
    usersList.add(user2)
    usersList.add(user)
    usersList.add(user)
    usersList.add(user)
    usersList.add(user2)


    Column( modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        usersList.forEach {
            userItem ->
            if( itemVisibilityCodition == userItem.blocked)
                UserItemCard(userItem,
                    onBlockChange = {user.blocked = it }, //TODO: wysyłąnie na serwer
                    failureMaxCount = value.toInt()
                )
        }


    }


}



@Composable
fun UserItemCard(userCard: UserCard,
                 onBlockChange: (Boolean) -> Unit,
                 failureMaxCount: Int = 100,
                 ) {
    val badColor = MaterialTheme.colorScheme.error
    val goodColor = MaterialTheme.colorScheme.primary
    var shadowColor = if (failureMaxCount >= userCard.failureCount) goodColor else badColor
    var checked by remember {  mutableStateOf(userCard.blocked) }

    LaunchedEffect(failureMaxCount) {
        shadowColor = if (failureMaxCount >= userCard.failureCount) goodColor else badColor
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
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
    ) {
        Column {
            Text("${userCard.id}      ${userCard.imie} ${userCard.nazwisko}")
            Text("Email: ${userCard.email}")
            Text("Błędnie wprowadzone dane: ${userCard.failureCount}")

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

