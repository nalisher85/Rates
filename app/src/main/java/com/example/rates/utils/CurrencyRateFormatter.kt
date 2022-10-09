package com.example.rates.utils

import java.text.DecimalFormat

object CurrencyRateFormatter {
    private val formatter = DecimalFormat(".####")

    fun format(number: Double): Double {
        return formatter.format(number).toDouble()
    }
}