package com.example.rates.popular.presentation

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rates.currency_rates.data.RatesRepository
import com.example.rates.currency_rates.data.Resource
import com.example.rates.currency_rates.data.cache.CurrencyRateEntity
import com.example.rates.currency_rates.data.network.RatesResponseData
import com.example.rates.sort.SortOptionsViewModel
import com.example.rates.ui.components.rates.UiCurrencyRate
import com.example.rates.utils.CurrencyRateFormatter
import com.example.rates.utils.sort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatesViewModel @Inject constructor(
    private val repository: RatesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        observeSelectedCurrency()
        observeFavorites()
        observeSortOption()
    }

    val onBaseCurrencySelected: (String) -> Unit = {
        _uiState.value = _uiState.value.copy(selectedCurrency = it)
    }

    val onFavoriteChecked: (UiCurrencyRate, Boolean) -> Unit = { item, isChecked ->
        viewModelScope.launch {
            if (isChecked) repository.addFavorite(
                CurrencyRateEntity(item.id, base = item.baseCurrency, quote = item.currency, item.currencyRate)
            )
            else repository.removeFavorite(item.id)
        }
    }

    val onSort: (SortOptionsViewModel.SortOption) -> Unit = {
        _uiState.value = _uiState.value.copy(sortOption = it)
    }

    val retryLoad: () -> Unit = {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            loadCurrencyRate(_uiState.value.selectedCurrency)
        }
    }

    private fun observeSortOption() {
        viewModelScope.launch {
            _uiState
                .map { it.sortOption }
                .distinctUntilChanged()
                .filterNotNull()
                .collectLatest { option ->
                    _uiState.value = _uiState.value.copy(currencyRates = _uiState.value.currencyRates.sort(option))
                }
        }
    }


    private fun observeSelectedCurrency() {
        viewModelScope.launch {
            _uiState
                .map { it.selectedCurrency }
                .distinctUntilChanged()
                .onEach { _uiState.value = _uiState.value.copy(isLoading = true) }
                .collectLatest { currency ->
                    loadCurrencyRate(currency)
                }
        }
    }

    private suspend fun loadCurrencyRate(currency: String) {
        repository.loadRates(currency).let { response ->
            if (response is Resource.Error) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = response.message)
            } else if (response is Resource.Success) {

                val favorites = repository.getFavoritesIntoMap()
                val currencyRates = response.data.toUiCurrencyRates(favorites)

                _uiState.value = _uiState.value.copy(
                    error = null,
                    isLoading = false,
                    currencyList = response.data.rates.map { it.key },
                    currencyRates = currencyRates
                )
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.getFavoritesIntoMapFlow().collectLatest { favorites ->
                val newList = margeUiCurrencyWithFavorite(_uiState.value.currencyRates, favorites)
                _uiState.value = _uiState.value.copy(currencyRates = newList)
            }
        }
    }

    // convert RatesResponseData to UiCurrencyRate and marge with favorites
    private fun RatesResponseData.toUiCurrencyRates(favorites: Map<String, CurrencyRateEntity>): List<UiCurrencyRate> {
        return rates.map {
            val id = "${base}/${it.key}"
            UiCurrencyRate(
                id = id,
                baseCurrency = base,
                currency = it.key,
                currencyRate = CurrencyRateFormatter.format(it.value),
                isFavorite = favorites.contains(id)
            )
        }
    }

    private fun margeUiCurrencyWithFavorite(
        uiCurrencyRates: List<UiCurrencyRate>,
        favorites: Map<String, CurrencyRateEntity>
    ): List<UiCurrencyRate> {

        val newList = mutableListOf<UiCurrencyRate>()

        uiCurrencyRates.forEach { uiCurrencyRate ->
            val containsInFavorite = favorites.contains(uiCurrencyRate.id)
            val newCurrencyRate = when {
                containsInFavorite && !uiCurrencyRate.isFavorite -> uiCurrencyRate.copy(isFavorite = true)
                !containsInFavorite && uiCurrencyRate.isFavorite -> uiCurrencyRate.copy(isFavorite = false)
                else -> uiCurrencyRate
            }
            newList.add(newCurrencyRate)
        }
        return newList
    }


    @Keep
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val selectedCurrency: String = "USD",
        val currencyList: List<String> = emptyList(),
        val currencyRates: List<UiCurrencyRate> = emptyList(),
        val sortOption: SortOptionsViewModel.SortOption? = null
    )
}