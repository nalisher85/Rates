package com.example.rates.currency_rates.data.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {

    /***
     * @param base  three-letter currency code of your preferred base currency.
     * @param symbols list of comma-separated currency codes to limit output currencies.
     */
    @GET("exchangerates_data/latest")
    suspend fun loadRates(
        @Query("base") base: String,
        @Query("symbols") symbols: String? = null
    ): Response<RatesResponseData>
}