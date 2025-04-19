package com.example.utampa.ui.theme.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items // Fix: Import items function
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.utampa.data.ParkingGarageDraw // Import the data source
import com.example.utampa.data.ParkingGarage // Import the ParkingGarage data class

@Composable
fun GuestParkingWidget() {
    // Fetch garages from ParkingGarageDraw.kt
    val garages: List<ParkingGarage> = ParkingGarageDraw.getParkingGarages()

    Column(modifier = Modifier.padding(16.dp)) {
        LazyRow {
            items(garages) { garage ->
                ParkingGarageCard(garage = garage) // Fix: Pass correct ParkingGarage object
            }
        }
    }
}


