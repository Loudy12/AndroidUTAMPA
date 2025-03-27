package com.example.utampa.ui.theme.pages

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun CampusScreen() {

    Text("Campus Screen", style = MaterialTheme.typography.headlineMedium)
    val context = LocalContext.current

    // Track location permission state
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission request launcher
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasLocationPermission = isGranted
        }

    // Request location permission if not granted
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Section
        Text(
            text = "Campus",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black
        )
        Text(
            text = "Welcome to the University of Tampa App",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Google Maps Preview (Clickable)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    context.startActivity(Intent(context, MapsActivity::class.java)) // Open full map
                },
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            if (hasLocationPermission) {
                GoogleMapView()
            } else {
                // Placeholder Text if permission is not granted
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Location permission required to display the map.", color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // List of buttons
        val buttons = listOf(
            "Buildings" to android.R.drawable.ic_menu_info_details,
            "Parking" to android.R.drawable.ic_menu_compass,
            "Dining" to android.R.drawable.ic_menu_manage,
            "Campus Map" to android.R.drawable.ic_menu_mapmode,
            "Recreations" to android.R.drawable.ic_menu_camera,
            "Academics" to android.R.drawable.ic_menu_agenda,
            "Directory" to android.R.drawable.ic_menu_call,
            "Campus Safety" to android.R.drawable.ic_menu_help,
            "Class Search" to android.R.drawable.ic_menu_search,
            "Campus Events" to android.R.drawable.ic_menu_today,
            "Feedback" to android.R.drawable.ic_menu_send
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            content = {
                items(buttons.size) { index ->
                    val button = buttons[index]
                    CampusButton(text = button.first, iconRes = button.second) {
                        when (button.first) {
                            "Campus Map" -> context.startActivity(Intent(context, MapsActivity::class.java))
                            //"Buildings" -> navController.navigate("buildingListScreen")
                        //   "Parking" -> navController.navigate("parkingList")
                          //  "Building" -> navController.navigate("buildingList")
                        }
                    }
                }
            }
        )
    }
}

@SuppressLint("MissingPermission") // We handle permission manually
@Composable
fun GoogleMapView() {
    val tampaLocation = LatLng(27.9506, -82.4572)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(tampaLocation, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = MapUiSettings(myLocationButtonEnabled = true)
    )
}

@Composable
fun CampusButton(text: String, iconRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .size(100.dp)
            .clickable { onClick() }, // Button is now clickable
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAEAEA)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                color = Color.Black
            )
        }
    }
}
