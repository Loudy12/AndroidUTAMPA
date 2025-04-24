package com.example.utampa.ui.theme.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utampa.R
import com.example.utampa.data.NewsItem
import com.example.utampa.ui.theme.components.CanvasWidget
import com.example.utampa.ui.theme.components.NewsWidget
import com.example.utampa.ui.theme.components.ParkingGarageWidget
import com.example.utampa.ui.theme.defBackground
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForYouScreen(navController: NavController) {
    val newsList = listOf(
        NewsItem("Swim The Seas at the RAC", "Monday, March 17th, 2025 8 AM", imageResId = R.drawable.rac_swim),
        NewsItem("New member meetings", "Wed, Apr 23, 2025 12 PM", imageResId = R.drawable.blank_news),
        NewsItem("Dogs & Donuts: Happy Tails x Active Minds", "Wed, Apr 23, 2025 12 PM", imageResId = R.drawable.blank_news),
        NewsItem("Qualtrics Basic Training", "Wed, Apr 23, 2025 1 PM", imageResId = R.drawable.qualtrics_pic),
        NewsItem("Zoom Basic Training", "Wed, Apr 23, 2025 2 PM", imageResId = R.drawable.zoom_pic),
        NewsItem("DSP X Flo Energy Sales Training", "Wed, Apr 23, 2025 3 PM", imageResId = R.drawable.blank_news),
        NewsItem("UT Boxing Club Meeting", "Wed, Apr 23, 2025 4PM", imageResId = R.drawable.boxing_club_pic),
        NewsItem("Ai for All: Unlocking Accessibility with ChatGPT", "Wed, Apr 23, 2025 4 PM", imageResId = R.drawable.ai_for_all_pic),
        NewsItem("Barry's Burn & Boost", "Wed, Apr 23, 2025 6 PM", imageResId = R.drawable.barrys_pic),
        NewsItem("Slime Time", "Wed, Apr 23, 2025 6 PM", imageResId = R.drawable.slime_time_pic),
        NewsItem("Banned Book Bingo", "Wed, Apr 23, 2025 4:30 PM", imageResId = R.drawable.blank_news),
        NewsItem("Greek Awards Night", "Wed, Apr 23, 2025 6 PM", imageResId = R.drawable.greek_awards_pic),
        NewsItem("Men's Bible Study", "Wed, Apr 23, 2025 6 PM", imageResId = R.drawable.bible_study_pic),
        NewsItem("Election Night", "Wed, Apr 23, 2025 6 PM", imageResId = R.drawable.election_pic),
        NewsItem("Earth Week Movie Night", "Wed, Apr 23, 2025 6 PM", imageResId = R.drawable.earth_movie_pic),

        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(defBackground)
    ) {

        Column {
            TopAppBar(
                title = {
                    Text("For You", style = MaterialTheme.typography.headlineMedium)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )

            // Bottom Border
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
        }

        //Scrollable Content with Background Color
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .background(defBackground)
        ) {
            // Latest News Section
            item {
                Text("Events", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                NewsWidget(newsList)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Parking Garages Section
            item {
                Text("Parking Garages", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier
                    .clickable {
                        navController.navigate("parking_screen")
                    }
                ) {
                    ParkingGarageWidget()
                }

                Spacer(modifier = Modifier.height(24.dp))
            }


            // Classes Section
            item {
                Text("Classes", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                CanvasWidget()
            }
        }
    }
}

@Preview
@Composable
fun ForYouScreenPreview() {
    ForYouScreen(navController = NavController(LocalContext.current))
}

