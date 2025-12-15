package com.example.balansapp.ui.service

import com.example.balansapp.ui.service.data.ChangePassword
import com.example.balansapp.ui.service.data.DaniaDetail
import com.example.balansapp.ui.service.data.LoginRequest
import com.example.balansapp.ui.service.data.LoginResponse
import com.example.balansapp.ui.service.data.PommiarWagii
import com.example.balansapp.ui.service.data.PrzyjacieleInfo
import com.example.balansapp.ui.service.data.RegisterRequest
import com.example.balansapp.ui.service.data.RegisterResponse
import com.example.balansapp.ui.service.data.SimpleMessage
import com.example.balansapp.ui.service.data.Uzytkownik
import com.example.balansapp.ui.service.data.ZaproszenieInfo
import com.example.firstcomposeap.ui.service.data.AllMealsInDay
import com.example.firstcomposeap.ui.service.data.Cwiczenie
import com.example.firstcomposeap.ui.service.data.MealUpdate
import com.example.firstcomposeap.ui.service.data.PlanTreningowy
import com.example.firstcomposeap.ui.service.data.Produkt
import com.example.firstcomposeap.ui.service.data.StatisticInterval
import com.example.firstcomposeap.ui.service.data.StatisticParameters
import com.example.firstcomposeap.ui.service.data.Trening
import com.example.firstcomposeap.ui.service.data.cwiczeniaPlanuTreningowegoResponse
import com.example.firstcomposeap.ui.service.data.treningsPlanCard
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


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
    : Response<SimpleMessage>

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

    @GET("uzytkownicy/friends")
    suspend fun getUserFrends(@Header("Authorization") authorization: String)
            : Response<List<PrzyjacieleInfo>>

    @GET("uzytkownicy/friends/accesed")
    suspend fun getUserFrendsICanModife(@Header("Authorization") authorization: String)
            : Response<List<PrzyjacieleInfo>>

    @HTTP(method = "DELETE", path = "uzytkownicy/friends", hasBody = true)
    suspend fun deleteUserFrend(@Body body: PrzyjacieleInfo, @Header("Authorization") authorization: String)
            : Response<List<SimpleMessage>>

    @PUT("uzytkownicy/friends")
    suspend fun changeAccessUserFrend(@Body body: PrzyjacieleInfo, @Header("Authorization") authorization: String)
            : Response<List<SimpleMessage>>

    @POST("uzytkownicy/recipe")
    suspend fun updateUserRecipes(@Body body: List<DaniaDetail>, @Header("Authorization") authorization: String)
            : Response<List<SimpleMessage>>

    @GET("uzytkownicy/recipe")
    suspend fun downloadUserRecipes( @Header("Authorization") authorization: String)
            : Response<List<DaniaDetail>>

    @POST("produkty/produkt")
    suspend fun addProductToDb(@Body body: Produkt) : Response<Produkt>

    @GET("produkty/produkt/{id}")
    suspend fun findProductById(
        @Path("id") id: Int
    ): Response<Produkt>


    /**
     *  Tworzenie albo modyfikacja posiłku użytkownika wymaga autoryzacji (token)
     */
    @POST("produkty/posilek")
    suspend fun createOrUpdateMeal(
        @Body body: MealUpdate
    ): Response<SimpleMessage>


    @GET("produkty/posilek/all")
    suspend fun getUserMealDay(
        @Query("date") date: String
    ): Response<AllMealsInDay>


    @GET("produkty/posilek/another/all")
    suspend fun getMealAnotherUser(
        @Query("date") date: String,
        @Query("userEmail") userEmail: String
    ): Response<AllMealsInDay>

    @POST("produkty/another/posilek")
    suspend fun createOrUpdateAnotherUserMeal(
        @Body body: MealUpdate,
        @Query("userEmail") userEmail: String
    ): Response<SimpleMessage>


    @GET("search/produkty")
    suspend fun getAllMatchesProduktNames(
        @Query("nazwa") nazwa: String
    ): Response<List<String>>

    @GET("search/cwiczenia")
    suspend fun getAllMatchesExerciseNames(
        @Query("nazwa") nazwa: String,
        @Query("grupyMiesiniowe") grupyMiesiniowe: List<String>?,
        @Query("dokladnosc") dokladnosc: Boolean
    ): Response<List<String>>

    @GET("search/cwiczenia/all")
    suspend fun getAllExercises(
        @Query("nazwa") nazwa: String,
        @Query("grupyMiesiniowe") grupyMiesiniowe: List<String>?,
        @Query("dokladnosc") dokladnosc: Boolean
    ): Response<List<Cwiczenie>>


    @GET("search/produkts/{nazwa}")
    suspend fun getAllMatchesProduct(
        @Path("nazwa") nazwa: String
    ): Response<List<Produkt>>


    /**
     * Obsługa Cwiczeń i treningów
     */

    @POST("trening/exercise/new")
    suspend fun createExercise(
        @Body body: Cwiczenie
    ): Response<Cwiczenie>

    @POST("trening/treningPlan")
    suspend fun createTreningPlan(
        @Body body: PlanTreningowy,
        @Query("aktualny") aktualny: Boolean = false
    ): Response<Cwiczenie>

    @POST("trening/treningPlan/update")
    suspend fun updateTreningPlan(
        @Body body: PlanTreningowy,
        @Query("aktualny") aktualny: Boolean = false
    ): Response<Cwiczenie>

    @GET("trening/treningPlan/{id}")
    suspend fun getExerciseTreningPlan(
        @Query("id") id: Int
    ): Response<List<cwiczeniaPlanuTreningowegoResponse>>

    @GET("trening/treningPlan")
    suspend fun getAllTreningPlans( ): Response<List<treningsPlanCard>>

    @GET("trening/new")
    suspend fun getAcctualTrening( ) : Response<Trening>



//    STATYSTYKI

    @POST("statistic/weight")
    suspend fun getStatisticUserWeight( @Body interval: StatisticInterval )
            : Response<List<StatisticParameters>>

}