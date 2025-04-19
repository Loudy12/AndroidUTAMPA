package com.example.utampa.ui.theme.pages

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.utampa.ui.theme.UtampaTheme
import com.example.utampa.ui.theme.guestview.GuestForYou
import com.example.utampa.ui.theme.guestview.GuestAroundTB
import com.example.utampa.ui.theme.guestview.GuestParkingView
import com.example.utampa.ui.theme.pages.SignInScreen

class GuestActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UtampaTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "guestForYou"
                ) {
                    composable("guestForYou") {
                        GuestForYou(navController)
                    }
                    composable("aroundTampaBay") {
                        GuestAroundTB(navController)
                    }
                    composable("guestParking") {
                        GuestParkingView(navController)
                    }
                }
            }
        }
    }
}
