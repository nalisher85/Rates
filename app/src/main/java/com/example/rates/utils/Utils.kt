package com.example.rates.utils

import com.example.rates.sort.SortOptionsViewModel
import com.example.rates.ui.components.rates.UiCurrencyRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun List<UiCurrencyRate>.sort(option: SortOptionsViewModel.SortOption): List<UiCurrencyRate> {
    return withContext(Dispatchers.Default) {
        when(option) {
            SortOptionsViewModel.SortOption.NAME_AZ -> sortedBy { it.currency }
            SortOptionsViewModel.SortOption.NAME_ZA -> sortedByDescending { it.currency }
            SortOptionsViewModel.SortOption.VALUE_ASCENDING -> sortedBy { it.currencyRate }
            SortOptionsViewModel.SortOption.VALUER_DESCENDING -> sortedByDescending { it.currencyRate }
        }
    }
}
