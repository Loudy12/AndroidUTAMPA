package com.example.utampa.ui.theme.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun CombineRowParkingCards() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ParkingGarageGuestFind(onClick = { /* Navigate to Parking Finder */ })
        ParkingPassWidget(onClick = { /* Navigate to Parking Pass */ })
    }
}
