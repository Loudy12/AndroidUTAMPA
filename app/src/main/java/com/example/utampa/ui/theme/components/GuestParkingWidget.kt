package com.example.utampa.ui.theme.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.utampa.data.ParkingGarage

@Preview
@Composable
fun GuestParkingWidget() {

    val garages = listOf(
        ParkingGarage("Thomas Garage", 75, "https://source.unsplash.com/300x200/?garage"),
        ParkingGarage("West Garage", 60, "https://source.unsplash.com/300x201/?parking"),
        ParkingGarage("Grand Garage", 90, "https://source.unsplash.com/300x202/?building"),
        ParkingGarage("Delaware Garage", 40, "https://source.unsplash.com/300x203/?cars")
    )

    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn {
            items(garages) { garage ->
                ParkingGarageCard(garage = garage)
            }
        }
    }
}

@Preview
@Composable
fun PreviewGuestParkingWidget() {
    ParkingGarageWidget()
}

