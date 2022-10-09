package com.example.rates.ui.components.rates

import androidx.annotation.Keep

@Keep
data class UiCurrencyRate(
    val id: String,
    val baseCurrency: String,
    val currency: String,
    val currencyRate: Double,
    val isFavorite: Boolean
)