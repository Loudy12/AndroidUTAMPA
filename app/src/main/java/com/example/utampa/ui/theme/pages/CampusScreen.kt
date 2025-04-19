package com.example.utampa.ui.theme.pages
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
@Composable
fun CampusScreen(navController: NavController) {
    val context = LocalContext.current
    // Location Permission
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasLocationPermission = isGranted
        }
    Log.d("CampusScreen", "******************************navController hash: ${navController.hashCode()}")
    //val navController = rememberNavController()

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
        // Google Maps Preview
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    context.startActivity(Intent(context, MapsActivity::class.java))
                },
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            if (hasLocationPermission) {
                GoogleMapView()
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Location permission required to display the map.", color = Color.Gray)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Buttons (Widgets)
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
            modifier = Modifier.fillMaxSize()
        ) {
            items(buttons) { button ->
                CampusButton(navController, text = button.first, iconRes = button.second) {
                    when (button.first) {
                        "Campus Map" -> context.startActivity(Intent(context, MapsActivity::class.java))
                        "Buildings" -> { Log.d("Button", "***********************navController hash: ${navController.hashCode()}")
                            if (navController.currentDestination?.route != "building_list_screen") {
                                navController.navigate("building_list_screen")
                            }
                        }
                        "Parking" -> {
                            if (navController.currentDestination?.route != "parking_screen") {
                                navController.navigate("parking_screen")
                            }
                        }
                        "Dining" -> {
                            if (navController.currentDestination?.route != "dining_list_screen") {
                                navController.navigate("dining_list_screen")
                            }
                        }
                        "Recreations" -> {
                            if (navController.currentDestination?.route != "recreation_screen") {
                                navController.navigate("recreation_screen")
                            }
                        }
                        "Academics" -> navController.navigate("academic_success_center_screen")
                        "Directory" -> navController.navigate("directory_screen")
                        "Campus Safety" -> navController.navigate("campus_safety_screen")
                        "Class Search" -> navController.navigate("class_search_screen")
                        "Campus Events" -> navController.navigate("campus_events_screen")
                        "Feedback" -> navController.navigate("feedback_screen")
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
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
fun CampusButton( navController: NavController, text: String, iconRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .size(100.dp)
            .clickable { onClick() },
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
            Text(text = text, fontSize = 12.sp, color = Color.Black)
        }
    }
}