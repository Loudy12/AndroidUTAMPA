package com.example.utampa.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ForYouScreen() {
    ScreenContent(title = "For You Screen")
}

@Composable
fun CampusScreen() {
    ScreenContent(title = "Campus Screen")
}

@Composable
fun ResourcesScreen() {
    ScreenContent(title = "Resources Screen")
}

@Composable
fun ProfileScreen() {
    ScreenContent(title = "Profile Screen")
}

@Composable
fun ScreenContent(title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
    }
}
