// CrousService.kt
package com.example.crous_project

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CrousService {
    @GET("/crous")
    fun getAllCrous(): Call<List<Crous>>

    //@GET("/crous/{id}")
    //fun getCrous(@Path("id") id: String): Call<Crous>
}
