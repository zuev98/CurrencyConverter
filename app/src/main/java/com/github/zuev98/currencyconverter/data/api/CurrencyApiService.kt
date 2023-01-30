package com.github.zuev98.currencyconverter.data.api

import retrofit2.Call
import retrofit2.http.GET

interface CurrencyApiService {
    @GET("daily_json.js")
    fun getCurrencies() : Call<String>
}