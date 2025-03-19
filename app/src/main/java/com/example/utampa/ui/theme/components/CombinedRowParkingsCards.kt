package com.example.utampa.ui.theme.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CombineRowParkingCards(navController: NavController) {
    val context = LocalContext.current // Get context once before using it

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Parking Finder Button
        ParkingGarageGuestFind(onClick = {
            openUrl(context, "https://www.ut.edu/about-ut/university-services/campus-safety/visitor-pass-form")
        })

        // Navigate to GuestParkingView (Fix Back Stack Issue)
        ParkingPassWidget(onClick = {
            navController.navigate("guest_parking_view") {
                popUpTo("home") { inclusive = false } // Ensure app doesn't exit
                launchSingleTop = true // Prevent duplicate screens
            }
        })
    }
}


fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}