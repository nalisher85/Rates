package com.example.rates.currency_rates.data.network


import androidx.annotation.Keep

@Keep
data class RatesResponseData(
    val base: String = "",
    val date: String = "",
    val rates: Map<String, Double> = emptyMap(),
    val success: Boolean = false,
    val timestamp: Int = 0,
    val error: Error? = null
)