package com.example.utampa.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.utampa.ui.theme.guestview.GuestAroundTB
import com.example.utampa.ui.theme.guestview.GuestForYou
import com.example.utampa.ui.theme.guestview.GuestParkingView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {

        composable("guest-home") {
            GuestForYou(navController)
        }

        composable("aroundTampaBay") {
            GuestAroundTB(navController)
        }
        // Guest Parking View Route (This is where ParkingPassWidget should navigate)
        composable("guest_parking_view") {
            GuestParkingView(navController)
        }
    }
}
