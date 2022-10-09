package com.example.rates.ui.components.rates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rates.R

@Composable
fun CurrencyRates(list: List<UiCurrencyRate>, onFavoriteClicked: (UiCurrencyRate) -> Unit) {
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 5.dp)) {
        items(list.size) { index ->
            val item = list[index]
            CurrencyRateItem(currencyRateModel = item) {
                onFavoriteClicked(item)
            }
        }
    }
}


@Composable
private fun CurrencyRateItem(currencyRateModel: UiCurrencyRate, onFavoriteClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currencyRateModel.currency,
            color = Color.Black,
            modifier = Modifier.width(60.dp)
        )
        Text(
            text = currencyRateModel.currencyRate.toString(),
            color = Color.Black,
        )
        Spacer(modifier = Modifier.weight(1f))

        val iconId = if (currencyRateModel.isFavorite) R.drawable.ic_favorite_selected else R.drawable.ic_favorite_unselected
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "favorite",
            modifier = Modifier.clickable { onFavoriteClicked() }
        )

    }
}


@Preview(showBackground = true)
@Composable
fun CurrencyRatesSectionPreview() {
    val list by remember {
        mutableStateOf(listOf(
            UiCurrencyRate(
                id = "USD/RUB",
                baseCurrency = "USD",
                currency = "RUB",
                currencyRate = 0.2548,
                isFavorite = false
            ),
            UiCurrencyRate(
                id = "USD/EUR",
                baseCurrency = "USD",
                currency = "EUR",
                currencyRate = 6.2,
                isFavorite = false
            ),
            UiCurrencyRate(
                id = "TJS/RUB",
                baseCurrency = "TJS",
                currency = "RUB",
                currencyRate = 85.2,
                isFavorite = false
            ),
            UiCurrencyRate(
                id = "UZS/TJS",
                baseCurrency = "UZS",
                currency = "TJS",
                currencyRate = 0.548,
                isFavorite = false
            ),
            UiCurrencyRate(
                id = "TRL/RUB",
                baseCurrency = "TRL",
                currency = "RUB",
                currencyRate = 5.54,
                isFavorite = false
            ),

            ))
    }

    MaterialTheme {
        CurrencyRates(list) { checked ->
        }
    }
}


//@Preview(showBackground = true)
@Composable
fun CurrencyRateItemPreview() {
    val currencyRate = UiCurrencyRate(
        id = "USD/RUB",
        baseCurrency = "USD",
        currency = "RUB",
        currencyRate = 70.2548,
        isFavorite = false
    )
    MaterialTheme {
        CurrencyRateItem(currencyRateModel = currencyRate, {})
    }
}
