package com.example.reshme_nammapride.ui.navigation

sealed class Screen(val route: String) {
    object Entry : Screen("entry")
    object History : Screen("history")
    object Archive : Screen("archive")
}