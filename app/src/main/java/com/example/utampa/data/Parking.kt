package com.example.utampa.models

import android.content.Context
import kotlinx.serialization.Serializable

@Serializable
data class Parking(
    val id: String,
    val abbreviation: String,
    val name: String,
    val type: String,
    val description: String,
    val imageName: String,
    val coordinates: Coordinates
) {
    @Serializable
    data class Coordinates(
        val latitude: Double,
        val longitude: Double
    )

    // Function to get image resource ID
    fun getImageResId(context: Context): Int {
        val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        return if (resourceId != 0) resourceId else context.resources.getIdentifier("underconstruction", "drawable", context.packageName)
    }
}