package com.example.utampa.helper

import android.content.Context
import com.example.utampa.models.Recreational
import kotlinx.serialization.json.Json

fun loadRecreationalData(context: Context, fileName: String = "recreationalData.json"): List<Recreational> {
    return try {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        Json { ignoreUnknownKeys = true }.decodeFromString<List<Recreational>>(jsonString)
    } catch (e: Exception) {
        emptyList()
    }
}
