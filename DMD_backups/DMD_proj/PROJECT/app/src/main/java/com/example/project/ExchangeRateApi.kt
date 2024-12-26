package com.example.project.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("v6/{apiKey}/latest/{base}")
    fun getExchangeRates(
        @Path("apiKey") apiKey: String,
        @Path("base") base: String
    ): Call<ExchangeRateResponse>
}