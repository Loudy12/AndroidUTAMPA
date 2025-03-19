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
import com.example.utampa.data.Dining
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiningDetailScreen(
    dining: Dining,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = dining.name, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = dining.getImageResId(context)),
                contentDescription = dining.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(screenHeight / 2)
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            )

            /* Map Feature (Currently Commented Out)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(
                    com.google.android.gms.maps.model.LatLng(dining.coordinates.latitude, dining.coordinates.longitude),
                    15f
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(zoomControlsEnabled = true, myLocationButtonEnabled = true),
                    properties = MapProperties(isMyLocationEnabled = false)
                ) {
                    Marker(
                        state = MarkerState(
                            position = com.google.android.gms.maps.model.LatLng(
                                dining.coordinates.latitude,
                                dining.coordinates.longitude
                            )
                        ),
                        title = dining.name
                    )
                }
            }
            */
        }
    }
}
