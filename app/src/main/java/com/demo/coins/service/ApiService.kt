package com.demo.coins.service

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.Call
import retrofit2.http.Query

interface ApiService {

    @GET("/v2/coins") // Endpoint
    fun getCoins(
        @Query("tier") tier: Int = 1,
    ): Call<CoinResponse>
}
