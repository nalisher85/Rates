package com.example.rates.currency_rates.data.cache

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rates.currency_rates.data.network.RatesResponseData
import com.example.rates.utils.CurrencyRateFormatter

@Keep
@Entity(tableName = "favorites")
data class CurrencyRateEntity(
    @PrimaryKey
    val id: String,
    val base: String,
    val quote: String,
    val rate: Double
)

fun RatesResponseData.toCurrencyRateEntity(): List<CurrencyRateEntity> {
    return rates.map {
        CurrencyRateEntity(
            id = "${base}/${it.key}",
            base = base,
            quote = it.key,
            rate = CurrencyRateFormatter.format(it.value),
        )
    }
}