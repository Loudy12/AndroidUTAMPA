package com.example.utampa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.utampa.ui.theme.UtampaTheme
import com.example.utampa.screens.CampusScreen
import com.example.utampa.screens.ResourcesScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UtampaTheme {
                BottomNavBarApp()
            }
        }
    }
}

@Composable
fun BottomNavBarApp() {
    var selectedItem by remember { mutableIntStateOf(0) }

    val items = listOf("For You", "Campus", "Resources", "Profile")
    val icons = listOf(
        Icons.Filled.Favorite,     // For you
        Icons.Filled.Home,  // Campus
        Icons.Filled.Info, // Resources
        Icons.Filled.Person    // Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (selectedItem) {
                0 -> ForYouScreen()
                1 -> CampusScreen()
                2 -> ResourcesScreen()
                3 -> ProfileScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    UtampaTheme {
        BottomNavBarApp()
    }
}
