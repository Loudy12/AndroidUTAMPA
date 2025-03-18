package com.example.utampa.data

import android.content.Context
import kotlinx.serialization.Serializable

@Serializable
data class Dining(
    val id: String,
    val name: String,
    val location: String,
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

    fun getImageResId(context: Context): Int {
        val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        return if (resourceId != 0) resourceId else context.resources.getIdentifier("underconstruction", "drawable", context.packageName)
    }
}