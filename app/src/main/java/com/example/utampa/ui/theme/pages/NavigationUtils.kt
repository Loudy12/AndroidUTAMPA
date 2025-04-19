// NavigationUtils.kt
package com.example.utampa.ui.theme.pages

import androidx.navigation.NavController

fun navigateBackToCampus(navController: NavController) {
    navController.navigate("campus_home") {
        popUpTo("campus_home") { inclusive = true }
    }
}