package com.example.utampa.data

import com.example.utampa.R

object ParkingGarageDraw {
    fun getParkingGarages(): List<ParkingGarage> {
        return listOf(
            ParkingGarage("Thomas Garage", 14, R.drawable.thomasparkinggarage),
            ParkingGarage("West Garage", 72, R.drawable.westparkinggarage),
            ParkingGarage("Delaware Garage", 26, R.drawable.delawareparkinggarage),
            ParkingGarage("Grand Garage", 57, R.drawable.grandcenter)
        )
    }
}

///connection point to be filled once sensors are up, connect into middle INT
