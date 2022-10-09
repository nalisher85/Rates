package com.example.rates.popular.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rates.navigation.Screens
import com.example.rates.sort.SortOptionsViewModel
import com.example.rates.ui.components.SelectCurrency
import com.example.rates.ui.components.rates.CurrencyRates
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PopularScreen(viewModel: RatesViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.error != null -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.error!!,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { viewModel.retryLoad() }) {
                    Text(text = "Повторить")
                }
            }
        }
        uiState.currencyRates.isNotEmpty() -> {

            LaunchedEffect(key1 = navController) {
                navController
                    .currentBackStackEntry
                    ?.savedStateHandle
                    ?.getStateFlow<SortOptionsViewModel.SortOption?>(SortOptionsViewModel.SORT_OPTION_ARGUMENT, null)
                    ?.collectLatest { option ->
                        option?.let { viewModel.onSort(it) }
                    }
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)) {

                SelectCurrency(
                    currency = uiState.selectedCurrency,
                    data = uiState.currencyList,
                    {
                        navController.navigate(Screens.SORT_OPTION.name)
                    }
                ) {
                    viewModel.onBaseCurrencySelected(it)
                }
                CurrencyRates(list = uiState.currencyRates) {item ->
                    viewModel.onFavoriteChecked(item, !item.isFavorite)
                }
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

