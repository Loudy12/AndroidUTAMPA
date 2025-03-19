package com.example.utampa.helper

import android.content.Context
import com.example.utampa.data.Dining
import kotlinx.serialization.json.Json
import java.io.IOException

fun loadDiningData(context: Context, fileName: String = "diningData.json"): List<Dining> {
    return try {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
    } catch (e: IOException) {
        emptyList()
    }
}
