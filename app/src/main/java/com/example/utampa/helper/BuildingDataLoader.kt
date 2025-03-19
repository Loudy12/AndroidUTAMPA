package com.example.utampa.helper

import android.content.Context
import com.example.utampa.data.Building
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun loadBuildings(context: Context, fileName: String = "buildingData.json"): List<Building> {
    val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    return Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
}
