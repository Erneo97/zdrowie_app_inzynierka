package com.example.balansapp.ui.service

import com.example.balansapp.ui.service.data.LoginRequest
import com.example.balansapp.ui.service.data.LoginResponse
import com.example.balansapp.ui.service.data.RegisterRequest
import com.example.balansapp.ui.service.data.RegisterResponse
import com.example.balansapp.ui.service.data.Uzytkownik
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {
    @POST("uzytkownicy")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("uzytkownicy/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("uzytkownicy/{id}")
    suspend fun getUser(
        @Path("id") userId: Int?
    ): Response<Uzytkownik>


}