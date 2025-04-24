package com.example.utampa.data

data class NewsItem(
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val imageResId: Int? = null
)