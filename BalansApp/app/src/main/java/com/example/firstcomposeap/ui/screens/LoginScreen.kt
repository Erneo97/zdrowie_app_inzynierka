package com.example.balansapp.ui.screens


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.balansapp.R
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.components.HeadText
import com.example.balansapp.ui.components.input.InputField
import com.example.balansapp.ui.components.input.LabeledCheckbox
import com.example.balansapp.ui.components.input.LoginBySocialmedia
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.components.input.PasswordInputField
import com.example.balansapp.ui.navigation.main.Screen
import com.example.balansapp.ui.service.LoginViewModel

@Composable
fun LoginScreen(navController: NavController,
                viewModel: LoginViewModel
) {
    var email by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var stateLogIn by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val user = viewModel.user
    val errorMessage = viewModel.errorMessage

    fun validateEmail(input: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return emailRegex.matches(input)
    }


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LogoBackGround()


        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeadText(
                fontSize = 48.sp,
            )
            Spacer(modifier = Modifier.height(14.dp))

            Text(text = context.getString(R.string.login_text))
            Spacer(modifier = Modifier.height(16.dp))
            InputField(value = email,
                isError = isError,
                onValueChange = {
                    email = it
                    isError = !validateEmail(it)
                },
                label =   context.getString(R.string.email)
            )

            Spacer(modifier = Modifier.height(16.dp))
            PasswordInputField(
                password = password,
                onPasswordChange = { password = it }
            )
            Row {
                LabeledCheckbox(
                    label = context.getString(R.string.state_log_in),
                    checked = stateLogIn,
                    onCheckedChange = { stateLogIn = it }
                )
                TextButton(onClick = {}) {
                    Text(text = context.getString(R.string.remind_pasd),
                        modifier = Modifier.clickable {},
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 20.sp,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }




            Spacer(modifier = Modifier.height(16.dp))
            FullSizeButton(
                text = context.getString(R.string.log_in),
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.login(email, password)

                    }
                    else {
                        Toast.makeText(context, context.getString(R.string.bad_login_form), Toast.LENGTH_SHORT).show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            FullSizeButton(
                text = context.getString(R.string.regster),
                onClick = {  navController.navigate(Screen.Register.route)}
            )

            Spacer(modifier = Modifier.height(32.dp))


            Spacer(modifier = Modifier.height(32.dp))
            Text(text = context.getString(R.string.login_socialmedia))
            Row (modifier = Modifier.fillMaxWidth().padding(40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                LoginBySocialmedia(id = R.drawable.logo_fb,
                    contentDescription = "Facebook",
                    onClick = {/* TODO obsługa logowanie facebook */}
                )

                LoginBySocialmedia(id = R.drawable.logo_inst,
                    contentDescription = "Instagram",
                    onClick = {/* TODO obsługa logowanie instagram */}
                )
            }
        }
    }

    user?.let {
        LaunchedEffect(it) {
            Toast.makeText(context, "Witaj, ${it.imie} ${it.nazwisko}!", Toast.LENGTH_LONG).show()
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    errorMessage?.let { error ->
        LaunchedEffect(error) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }
}