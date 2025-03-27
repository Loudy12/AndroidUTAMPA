package com.example.utampa.data

import android.content.Context
import kotlinx.serialization.Serializable

@Serializable
data class Building(
    val id: String,
    val name: String,
    val abbreviation: String,
    val type: String,
    val description: String,
    val imageName: String,
    val coordinates: Coordinates
) {
    @Serializable
    data class Coordinates(
        val longitude: Double,
        val latitude: Double
    )

    /**
     * Returns the drawable resource ID for this building's image.
     */
    fun getImageResId(context: Context): Int {
        val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)

        if (resourceId != 0) {
            return resourceId

        } else {
            return context.resources.getIdentifier("underconstruction", "drawable", context.packageName)

        }
    }
}
