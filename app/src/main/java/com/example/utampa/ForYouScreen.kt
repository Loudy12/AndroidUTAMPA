package com.example.utampa

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utampa.data.NewsItem
import com.example.utampa.ui.components.CanvasWidget
import com.example.utampa.ui.theme.components.NewsWidget
import com.example.utampa.ui.theme.components.ParkingGarageWidget
import com.example.utampa.ui.theme.defBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForYouScreen() {
    val newsList = listOf(
        NewsItem("UTampa Wins!", "The University of Tampa secures the first place!", "https://source.unsplash.com/random/300x200"),
        NewsItem("New Research at UT", "Scientists discover innovative technology.", "https://source.unsplash.com/random/300x201"),
        NewsItem("Campus Expansion", "UT to add new buildings and dorms!", "https://source.unsplash.com/random/300x202")
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
                actions = {
                    IconButton(onClick = { /* Handle notification click */ }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_bell),
                            contentDescription = "Notifications",
                            modifier = Modifier.size(24.dp)
                        )
                    }
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
                Text("Latest News", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                NewsWidget(newsList)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Parking Garages Section
            item {
                Text("Parking Garages", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                ParkingGarageWidget()
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
fun PreviewForYouScreen() {
    ForYouScreen()
}
