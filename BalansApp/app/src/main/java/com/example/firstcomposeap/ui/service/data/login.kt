package com.example.balansapp.ui.service.data

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val id: Int,
    val token: String
)

