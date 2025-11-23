package com.example.balansapp.ui.service.data

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val email: String
)

data class ChangePassword(
    val oldPassword: String,
    val newPassword: String
)


data class SimpleMessage(val message: String)