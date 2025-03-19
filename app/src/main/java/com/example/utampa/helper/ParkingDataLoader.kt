package com.example.utampa.helper

import android.content.Context
import com.example.utampa.models.Parking
import kotlinx.serialization.json.Json

fun loadParkingData(context: Context, fileName: String = "parkingData.json"): List<Parking> {
    return try {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
    } catch (e: Exception) {
        emptyList()
    }
}
