package com.example.firstcomposeap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.service.LoginViewModel

/**
 * Ekran dodawania nowego przepisu (Dania) do konta użytkownika, jego prywatne przepisy dostepne tylko dla niego.
 * Pozwala szybko uzupełniać maro danego dnia
 */
@Composable
fun NewRecipeScreen(loginViewModel: LoginViewModel,
                    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            NavigationButtonsRetAdd(
                onClose = { onClose() },
                onAdd = {
//                    TODO()
                },
                mainText = "Zapisz posiłek"
            )
        }

        HeadText(text = "Nowy posilek", fontSize = 25.sp)

    }
}

