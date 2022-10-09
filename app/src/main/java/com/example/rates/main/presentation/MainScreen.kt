package com.example.rates.main.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rates.navigation.Navigation
import com.example.rates.navigation.Screen

@Composable
fun MainScreen(navController: NavHostController) {
    val screens = remember {
        listOf(
            Screen.Popular,
            Screen.Favorite
        )
    }
    
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    BottomNavigationItem(
                        selected = currentDestination?.hierarchy?.any { it.route == screen.rout } == true,
                        //label = { },
                        onClick = {
                            navController.navigate(screen.rout) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text(text = stringResource(id = screen.resourceId)) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Navigation(navController, modifier = Modifier.padding(paddingValues))
    }
    
}