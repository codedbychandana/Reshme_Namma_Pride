package com.example.reshme_nammapride.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.reshme_nammapride.ui.screens.entry.EntryScreen
import com.example.reshme_nammapride.ui.screens.history.ArchiveScreen
import com.example.reshme_nammapride.ui.screens.history.HistoryScreen
import com.example.reshme_nammapride.viewmodel.ClimateViewModel
import com.example.reshme_nammapride.viewmodel.ManagementViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    climateViewModel:  ClimateViewModel,
    managementViewModel: ManagementViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Entry.route
    ) {
        composable(Screen.Entry.route) {
            EntryScreen(climateViewModel, managementViewModel)
        }
        composable(Screen.History.route) {
            HistoryScreen(viewModel = climateViewModel, navController = navController)
        }
        composable(Screen.Archive.route) {
            ArchiveScreen(
                viewModel = climateViewModel,
                onBack = { navController.popBackStack() },
                onBatchClick = { batchId ->
                    climateViewModel.selectBatch(batchId)
                    navController.navigate(Screen.History.route)
                }
            )
        }
    }
}