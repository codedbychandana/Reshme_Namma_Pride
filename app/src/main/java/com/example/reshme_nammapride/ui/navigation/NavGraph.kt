package com.example.reshme_nammapride.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.reshme_nammapride.ui.screens.entry.EntryScreen
import com.example.reshme_nammapride.ui.screens.history.HistoryScreen
import com.example.reshme_nammapride.viewmodel.ClimateViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: ClimateViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Entry.route
    ) {
        composable(Screen.Entry.route) {
            EntryScreen(viewModel = viewModel)
        }
        composable(Screen.History.route) {
            HistoryScreen(viewModel = viewModel)
        }
    }
}