package com.example.utampa.ui.theme.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.utampa.data.Building
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.google.maps.android.compose.*
import androidx.compose.material.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.border
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBar
import androidx.navigation.NavController
import com.example.utampa.models.Parking


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkingDetailScreen(navController: NavController,
                        parking: Parking,
                        onBack: () -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Uses building.name with titleLarge typography style
                    Text(
                        text = parking.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                // Matches the color scheme from BuildingListScreen
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }
    )
    { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top half: building image

            // Updated Image Modifier:
            // - fillMaxWidth(0.95f) leaves a gap on both sides to show the background.
            // - align(Alignment.CenterHorizontally) centers the image.
            // - Added top padding so that the image doesn't start flush at the very top.
            Image(
                painter = painterResource(id = parking.getImageResId(context)),
                contentDescription = parking.name,
                contentScale = ContentScale.Crop, // Adjust scaling as desired (Crop maintains aspect ratio and fills space)
                modifier = Modifier
                    .fillMaxWidth(0.95f) // Changed from fillMaxWidth() to leave gaps on the sides
                    .height(screenHeight / 2)
                    .clip(RoundedCornerShape(8.dp)) // Rounded corners
                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .align(Alignment.CenterHorizontally) // Center the image horizontally
                    .padding(top = 8.dp) // Added top padding for extra gap from the top edge
            )
        }
    }
}
