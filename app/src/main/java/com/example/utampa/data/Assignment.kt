package com.example.utampa.data

import java.text.SimpleDateFormat
import java.util.*

data class Assignment(
    val id: Int,
    val name: String,
    val due: String?
) {
    // Computed property for formatted due date
    val formattedDueDate: String
        get() {
            due ?: return "No due date"
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                val date = inputFormat.parse(due)
                outputFormat.format(date ?: return "Invalid date")
            } catch (e: Exception) {
                "Invalid date"
            }
        }
}
