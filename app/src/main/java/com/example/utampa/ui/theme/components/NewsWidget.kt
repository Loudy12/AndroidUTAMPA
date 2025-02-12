package com.example.utampa.ui.theme.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.utampa.data.NewsItem

@Composable
fun NewsWidget(newsList: List<NewsItem>) {
    Column(modifier = Modifier.padding(16.dp)) {


        LazyRow {
            items(newsList.size) { index ->
                val news = newsList[index]
                NewsCard(news)
            }
        }
    }
}

@Composable
fun NewsCard(news: NewsItem) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(220.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column {
            AsyncImage(
                model = news.imageUrl,
                contentDescription = "News Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(news.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(news.description, fontSize = 14.sp, color = Color.Gray, maxLines = 2)
            }
        }
    }
}

@Preview
@Composable
fun PreviewNewsWidget() {
    val sampleNews = listOf(
        NewsItem("UTampa Wins Championship", "The University of Tampa secures the first place!", "https://source.unsplash.com/random/300x200"),
        NewsItem("New Research at UT", "Scientists discover innovative technology.", "https://source.unsplash.com/random/300x201"),
        NewsItem("Campus Expansion", "UT to add new buildings and dorms!", "https://source.unsplash.com/random/300x202")
    )

    NewsWidget(sampleNews)
}

