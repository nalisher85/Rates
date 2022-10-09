package com.example.rates.favorite.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rates.navigation.Screens
import com.example.rates.sort.SortOptionsViewModel
import com.example.rates.ui.components.SelectCurrency
import com.example.rates.ui.components.rates.CurrencyRates
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FavoriteScreen(viewModel: FavoriteViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 15.dp)) {

        SelectCurrency(
            currency = uiState.selectedCurrency,
            data = uiState.currencyList,
            onSortClicked = { navController.navigate(Screens.SORT_OPTION.name) }
        ) {
            viewModel.onCurrencySelected(it)
        }

        LaunchedEffect(key1 = navController) {
            navController
                .currentBackStackEntry
                ?.savedStateHandle
                ?.getStateFlow<SortOptionsViewModel.SortOption?>(SortOptionsViewModel.SORT_OPTION_ARGUMENT, null)
                ?.collectLatest { option ->
                    option?.let { viewModel.onSort(it) }
                }
        }

        when {
            uiState.isLoading && uiState.currencyRates.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = uiState.error!!)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = { viewModel.retryLoad() }) {
                        Text(
                            text = "Повторить",
                        )
                    }
                }
            }
            uiState.selectedCurrency.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Select currency")
                }
            }
            uiState.currencyRates.isNotEmpty() -> {
                CurrencyRates(list = uiState.currencyRates) {item ->
                    viewModel.onRemoveFavorite(item.id)
                }
            }
            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Data :(")
                }
            }
        }

    }


}