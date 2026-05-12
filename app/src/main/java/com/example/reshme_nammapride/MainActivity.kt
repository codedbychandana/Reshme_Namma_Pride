package com.example.reshme_nammapride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.reshme_nammapride.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.reshme_nammapride.data.local.AppDatabase
import com.example.reshme_nammapride.ui.navigation.NavGraph
import com.example.reshme_nammapride.ui.navigation.Screen
import com.example.reshme_nammapride.ui.theme.ReshmeNammaPrideTheme
import com.example.reshme_nammapride.viewmodel.ClimateViewModel
import com.example.reshme_nammapride.viewmodel.ManagementViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "reshme_database"
        ).fallbackToDestructiveMigration(false).build()

        val rearingDao = db.rearingDao()

        setContent {
            ReshmeNammaPrideTheme {
                val navController = rememberNavController()

                val climateViewModel: ClimateViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return ClimateViewModel(rearingDao) as T
                        }
                    }
                )

                val managementViewModel: ManagementViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return ManagementViewModel() as T
                        }
                    }
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route

                            NavigationBarItem(
                                selected = currentRoute == Screen.Entry.route,
                                onClick = { navController.navigate(Screen.Entry.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }},
                                icon = { Icon(painter = painterResource(R.drawable.log), "log") },
                                label = { Text("Log") }
                            )
                            NavigationBarItem(
                                selected = currentRoute == Screen.History.route,
                                onClick = {
                                    climateViewModel.selectBatch(null)
                                    navController.navigate(Screen.History.route) {
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        modifier = Modifier.size(32.dp),
                                        painter = painterResource(R.drawable.history),
                                        contentDescription = "History"
                                    )
                                },
                                label = { Text("History") }
                            )
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        NavGraph(navController = navController, climateViewModel = climateViewModel, managementViewModel = managementViewModel)
                    }
                }
            }
        }
    }
}