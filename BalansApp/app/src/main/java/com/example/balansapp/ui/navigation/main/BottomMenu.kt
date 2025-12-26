package com.example.balansapp.ui.navigation.main

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.balansapp.R
import com.example.balansapp.ui.components.icon.FontAwesomeUsers
import com.example.balansapp.ui.components.icon.MaterialSymbolsBriefcase_meal


@Composable
fun BottomMenu(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val items = listOf(
        MenuItem(context.getString(R.string.menu_meal), drawableRes = R.drawable.apple),
        MenuItem(context.getString(R.string.menu_trenings), drawableRes = R.drawable.calendar),
        MenuItem(context.getString(R.string.menu_profil), drawableRes = R.drawable.profile),
        MenuItem(context.getString(R.string.menu_plans), drawableRes = R.drawable.fitness_center_rounded)
    )

    NavigationBar(tonalElevation = 8.dp) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    when {
                        item.imageVector != null -> Icon(
                            imageVector = item.imageVector,
                            contentDescription = item.title
                        )
                        item.drawableRes != null -> Icon(
                            painter = painterResource(id = item.drawableRes),
                            contentDescription = item.title
                        )
                    }
                },
                label = { Text(item.title) },
                selected = selectedItem == item.title,
                onClick = { onItemSelected(item.title) }
            )
        }
    }
}

@Composable
fun BottomAdminMenu(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val items = listOf(
        MenuItem(context.getString(R.string.users), imageVector = FontAwesomeUsers),
        MenuItem(context.getString(R.string.products), imageVector = MaterialSymbolsBriefcase_meal),
        MenuItem(context.getString(R.string.exercises), drawableRes = R.drawable.fitness_center_rounded),
        MenuItem(context.getString(R.string.menu_profil), drawableRes = R.drawable.profile),
    )

    NavigationBar(tonalElevation = 8.dp) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    when {
                        item.imageVector != null -> Icon(
                            imageVector = item.imageVector,
                            contentDescription = item.title
                        )
                        item.drawableRes != null -> Icon(
                            painter = painterResource(id = item.drawableRes),
                            contentDescription = item.title
                        )
                    }
                },
                label = { Text(item.title) },
                selected = selectedItem == item.title,
                onClick = { onItemSelected(item.title) }
            )
        }
    }
}

data class MenuItem(
    val title: String,
    val imageVector: ImageVector? = null,
    val drawableRes: Int? = null
)
