package com.example.balansapp.ui.screens


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.balansapp.R
import com.example.balansapp.ui.components.FullSizeButton
import com.example.balansapp.ui.components.GenderSelectBox
import com.example.balansapp.ui.components.input.InputField
import com.example.balansapp.ui.components.input.LogoBackGround
import com.example.balansapp.ui.components.input.PasswordInputField
import com.example.balansapp.ui.components.input.validateEmail
import com.example.balansapp.ui.service.RegisterViewModel
import com.example.balansapp.ui.navigation.main.Screen

import kotlin.text.isEmpty
import kotlin.text.isNotEmpty


@Composable
fun RegisterScreen(navController: NavController,
                   viewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordError by remember { mutableStateOf(false) }
    val registerState = viewModel.registerState
    val errorMessage = viewModel.errorMessage

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LogoBackGround()

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()) ,
            verticalArrangement = Arrangement.Center,

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = context.getString(R.string.register_text), fontSize = 38.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(25.dp))
            InputField(
                value = name,
                onValueChange = {  name = it },
                label = context.getString(R.string.name)
            )
            Spacer(modifier = Modifier.height(16.dp))
            InputField(
                value = surname,
                onValueChange = {  surname = it },
                label = context.getString(R.string.surname)
            )

            Spacer(modifier = Modifier.height(16.dp))
            InputField(
                value = email,
                isError = isError,
                onValueChange = {
                    email = it
                    isError = !validateEmail(it)
                },
                label = context.getString(R.string.email)
            )
            Spacer(modifier = Modifier.height(25.dp))

            PasswordInputField(
                password = password,
                onPasswordChange = {
                    password = it
                    isPasswordError = confirmPassword.isNotEmpty() && password != confirmPassword
                },
                passwordError = isPasswordError,
                label = context.getString(R.string.password)
            )
            Spacer(modifier = Modifier.height(15.dp))
            PasswordInputField(
                password = confirmPassword,
                onPasswordChange = {
                    confirmPassword = it
                    isPasswordError = confirmPassword.isNotEmpty() && password != confirmPassword
                },
                passwordError = isPasswordError,
                label = context.getString(R.string.confirm_password)
            )
            Spacer(modifier = Modifier.height(15.dp))

            var selectedGender by remember { mutableStateOf("") }

            GenderSelectBox(
                selectedGender = selectedGender,
                onGenderSelected = { selectedGender = it }
            )



            Spacer(modifier = Modifier.height(40.dp))
            FullSizeButton(
                text = "Zarejestruj się",
                onClick = {
                    /* TODO obsługa Rejestracji */
                    var isAnyFormatError: Boolean = false;

                    if (isError || email.isEmpty()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.register_formEmail),
                            Toast.LENGTH_LONG
                        ).show()
                        isAnyFormatError = true
                    }
                    if (isPasswordError || password.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(context, context.getString(R.string.register_password), Toast.LENGTH_LONG)
                            .show()
                        isAnyFormatError = true
                    }
                    if (selectedGender.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                        Toast.makeText(context, context.getString(R.string.register_emptyInput), Toast.LENGTH_LONG)
                            .show()
                        isAnyFormatError = true
                    }

                    if( !isAnyFormatError) {
                        viewModel.registerUser(name, surname, email, password, selectedGender)
                        navController.navigate(Screen.Login.route)
                    }

                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            FullSizeButton(
                text = context.getString(R.string.anuluj),
                onClick = {  navController.navigate(Screen.Login.route) }
            )
            Spacer(modifier = Modifier.height(115.dp))

        }
    }

    registerState?.let { response ->
        LaunchedEffect(response) {
            Toast.makeText(context, "Rejestracja przebiegła pomyślnie", Toast.LENGTH_LONG).show()
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Register.route) { inclusive = true }
            }
        }
    }

    errorMessage?.let { error ->
        LaunchedEffect(error) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }
}