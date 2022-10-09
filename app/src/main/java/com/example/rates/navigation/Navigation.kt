package com.example.rates.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rates.favorite.presentation.FavoriteScreen
import com.example.rates.favorite.presentation.FavoriteViewModel
import com.example.rates.popular.presentation.PopularScreen
import com.example.rates.popular.presentation.RatesViewModel
import com.example.rates.sort.SortOptionScreen
import com.example.rates.sort.SortOptionsViewModel

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(
        navController = navController,
        startDestination = Screen.Popular.rout,
        modifier = modifier
    ) {

        composable(Screen.Popular.rout) {
            val viewModel = hiltViewModel<RatesViewModel>()
            PopularScreen(viewModel, navController)
        }

        composable(Screen.Favorite.rout) {
            val viewModel = hiltViewModel<FavoriteViewModel>()
            FavoriteScreen(viewModel, navController)
        }

        composable(Screens.SORT_OPTION.name) {
            val viewModel = hiltViewModel<SortOptionsViewModel>()
            SortOptionScreen(viewModel = viewModel, navController = navController)
        }
    }
}