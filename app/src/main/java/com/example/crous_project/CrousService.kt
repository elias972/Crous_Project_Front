// CrousService.kt
package com.example.crous_project

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface CrousService {
    @GET("/crous")
    fun getAllCrous(): Call<List<Crous>>

    @POST("/crous")
    fun createCrous(@Body crous: Crous): Call<Crous>

    //@GET("/crous/{id}")
    //fun getCrous(@Path("id") id: String): Call<Crous>
    @PUT("crous/favorite/{id}")
    fun toggleFavorite(@Path("id") id: String): Call<Void>


}
