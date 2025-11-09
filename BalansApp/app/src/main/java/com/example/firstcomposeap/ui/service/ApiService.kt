package com.example.balansapp.ui.service

import com.example.balansapp.ui.service.data.ChangePassword
import com.example.balansapp.ui.service.data.LoginRequest
import com.example.balansapp.ui.service.data.LoginResponse
import com.example.balansapp.ui.service.data.PommiarWagii
import com.example.balansapp.ui.service.data.RegisterRequest
import com.example.balansapp.ui.service.data.RegisterResponse
import com.example.balansapp.ui.service.data.SimpleMessage
import com.example.balansapp.ui.service.data.Uzytkownik
import com.example.balansapp.ui.service.data.ZaproszenieInfo
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT


interface ApiService {
    @POST("uzytkownicy")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("uzytkownicy/login")
    suspend fun login(@Body body: LoginRequest, @Header("Authorization") authorization: String): Response<LoginResponse>

    @GET("uzytkownicy/user")
    suspend fun getUser( @Header("Authorization") authHeader: String
    ): Response<Uzytkownik>

    @POST("uzytkownicy/waga")
    suspend fun addUserWeigt(@Body body: PommiarWagii, @Header("Authorization") authorization: String)
    : Response<String>

    @PUT("uzytkownicy/update")
    suspend fun updateBasicInformationUser( @Body body: Uzytkownik, @Header("Authorization") authorization: String)
    : Response<ResponseBody>

    @PUT("uzytkownicy/password")
    suspend fun updatePasswordUser( @Body body: ChangePassword, @Header("Authorization") authorization: String)
            : Response<SimpleMessage>


    @POST("uzytkownicy/invitation/new")
    suspend fun inviteFriend(@Body body: String, @Header("Authorization") authorization: String)
    : Response<SimpleMessage>

    @GET("uzytkownicy/invitation/all")
    suspend fun getAllInvitationUser(@Header("Authorization") authorization: String)
            : Response<List<ZaproszenieInfo>>


    @PUT("uzytkownicy/invitation/accept")
    suspend fun akceptInvitation( @Body body: ZaproszenieInfo, @Header("Authorization") authorization: String)
            : Response<SimpleMessage>

    @PUT("uzytkownicy/invitation/del")
    suspend fun cancelInvitation( @Body body: ZaproszenieInfo, @Header("Authorization") authorization: String)
            : Response<SimpleMessage>
}