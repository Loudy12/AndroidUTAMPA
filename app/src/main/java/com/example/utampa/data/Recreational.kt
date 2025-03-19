package com.example.utampa.models

import android.content.Context
import kotlinx.serialization.Serializable

@Serializable
data class Recreational(
    val id: String,
    val abbreviation: String,
    val name: String,
    val type: String,
    val description: String,
    val coordinates: Coordinates,
    val imageName: String
) {
    @Serializable
    data class Coordinates(
        val latitude: Double,
        val longitude: Double
    )

    fun getImageResId(context: Context): Int {
        val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        return if (resourceId != 0) resourceId else context.resources.getIdentifier("underconstruction", "drawable", context.packageName)
    }
}