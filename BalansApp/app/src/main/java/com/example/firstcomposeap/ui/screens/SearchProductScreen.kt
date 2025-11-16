package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.balansapp.ui.service.LoginViewModel

@Composable
fun SearchProductScreen(onClose: () -> Unit,
                        loginViewModel: LoginViewModel ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Wyszukaj produkt", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = loginViewModel.searchQuery,
            onValueChange = { loginViewModel.searchQuery = it },
            placeholder = { Text("Nazwa produktu...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onClose() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Zamknij")
        }
    }
}
