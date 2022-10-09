package com.example.rates.favorite.presentation

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rates.currency_rates.data.RatesRepository
import com.example.rates.currency_rates.data.Resource
import com.example.rates.currency_rates.data.cache.CurrencyRateEntity
import com.example.rates.currency_rates.data.cache.toCurrencyRateEntity
import com.example.rates.sort.SortOptionsViewModel
import com.example.rates.ui.components.rates.UiCurrencyRate
import com.example.rates.utils.sort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: RatesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    val onCurrencySelected: (String) -> Unit = {
        _uiState.value = _uiState.value.copy(selectedCurrency = it)
    }

    val onRemoveFavorite: (String) -> Unit = {
        viewModelScope.launch { repository.removeFavorite(it) }
    }

    val onSort: (SortOptionsViewModel.SortOption) -> Unit = {
        _uiState.value = _uiState.value.copy(sortOption = it)
    }

    fun retryLoad() {
        viewModelScope.launch { getFavorites(_uiState.value.selectedCurrency) }
    }


    init {

        // populate  currency for select field
        viewModelScope.launch {
            repository.getFavoritesFlow()
                .distinctUntilChanged { old, new -> old.size == new.size }
                .map { favorites ->
                    mutableSetOf<String>().apply { addAll(favorites.map { it.base }) }
                }.collectLatest { currencySet ->
                    val selectedCurrency = _uiState.value.selectedCurrency.ifBlank { currencySet.firstOrNull() } ?: ""
                    _uiState.value = _uiState.value.copy(currencyList = currencySet.toList(), selectedCurrency = selectedCurrency)
                }
        }

        observeSelectedCurrency()
        observeFavorites()
        observeSortOption()

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
                .filterNot { it.isBlank() }
                .distinctUntilChanged()
                .collectLatest { getFavorites(it) }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.getFavoritesFlow()
                .map { it.filter { fav -> fav.base == _uiState.value.selectedCurrency } }
                .collectLatest { favorites: List<CurrencyRateEntity> ->

                    val currencyRates = favorites.map { favorite -> favorite.toUiCurrencyRate() }.let {
                        if (_uiState.value.sortOption != null) it.sort(_uiState.value.sortOption!!) else it
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null,
                        currencyRates = currencyRates
                    )
                }
        }
    }

    private suspend fun getFavorites(currency: String) {
        val favorites = repository.getFavoritesByBase(currency)
        if (favorites.isEmpty())
            _uiState.value = _uiState.value.copy(isLoading = false, error = "No favorites", currencyRates = emptyList())
        else syncCurrencyRates(favorites)


    }

    private suspend fun syncCurrencyRates(favorites: List<CurrencyRateEntity>) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null, currencyRates = emptyList())

        val symbols = favorites.joinToString(",") { it.quote }
        val response = repository.loadRates(_uiState.value.selectedCurrency, symbols)

        if (response is Resource.Error) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = response.message)
        } else if (response is Resource.Success) {
            val currencyRateEntities = response.data.toCurrencyRateEntity()
            repository.addFavorites(currencyRateEntities)
        }
    }

    private fun CurrencyRateEntity.toUiCurrencyRate(): UiCurrencyRate {
       return UiCurrencyRate(
            id = id,
            baseCurrency = base,
            currency = quote,
            currencyRate = rate,
            isFavorite = true
        )
    }

    @Keep
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val selectedCurrency: String = "",
        val currencyList: List<String> = emptyList(),
        val currencyRates: List<UiCurrencyRate> = emptyList(),
        val sortOption: SortOptionsViewModel.SortOption? = null
    )
}