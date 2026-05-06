package com.example.reshme_nammapride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.reshme_nammapride.data.local.AppDatabase
import com.example.reshme_nammapride.ui.screens.entry.EntryScreen
import com.example.reshme_nammapride.ui.theme.ReshmeNammaPrideTheme
import com.example.reshme_nammapride.viewmodel.ClimateViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize Database
        val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "reshme_database"
            ).fallbackToDestructiveMigration(false).build()

        val rearingDao = db.rearingDao()

        setContent {
            ReshmeNammaPrideTheme {
                Surface {
                    // Create viewModel
                    val climateViewModel: ClimateViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return ClimateViewModel(rearingDao) as T
                            }
                        }
                    )

                    EntryScreen(viewModel = climateViewModel)
                }
            }
        }
    }
}

