package com.example.lab43.model

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("pizza")
    suspend fun getPizzas(): List<Pizza>

    @POST("pizza")
    fun createPizza(@Body requestData: RequestBody): Call<String>

    @PATCH("pizza/{id}")
    fun updatePizza(
        @Path("id") id: Long,
        @Body requestBody: RequestBody
    ): Call<String>
}
