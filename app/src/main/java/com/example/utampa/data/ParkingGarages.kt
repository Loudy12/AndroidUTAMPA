package com.example.utampa.data

import androidx.annotation.DrawableRes

data class ParkingGarage(
    val name: String,
    val capacityPercentage: Int,
    @DrawableRes val imageResId: Int
)
