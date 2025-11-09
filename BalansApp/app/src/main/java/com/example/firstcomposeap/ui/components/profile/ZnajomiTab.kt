package com.example.firstcomposeap.ui.components.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.R

@Composable
fun ZnajomiTab () {
    Spacer(modifier = Modifier.height(30.dp))
    val context = LocalContext.current
    val tabs = listOf(context.getString(R.string.friends)
        , context.getString(R.string.invitation))

    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontSize = 22.sp) }
                )
            }
        }

        when (selectedTabIndex ) {
            0 -> {Text("Znajomi lista")}
            1 -> {Text("Zaproszenia lista")}
        }
    }


}