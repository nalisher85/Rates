package com.example.rates.ui.components.rates

import androidx.annotation.Keep
import androidx.compose.runtime.Stable

@Keep
@Stable
data class UiCurrencyRate(
    val id: String,
    val baseCurrency: String,
    val currency: String,
    val currencyRate: Double,
    val isFavorite: Boolean
)