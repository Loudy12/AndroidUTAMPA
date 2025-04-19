package com.example.utampa.ui.theme.pages


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview
import com.example.utampa.AWS.AWSIAMCredentialsManager
import com.example.utampa.AWS.AuthController
import com.example.utampa.AWS.CognitoHelper
import com.example.utampa.UTampaApp
import com.example.utampa.ui.theme.UtampaTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.utampa.helper.loadBuildings
import com.example.utampa.helper.loadDiningData
import com.example.utampa.helper.loadParkingData
import com.example.utampa.helper.loadRecreationalData
import com.example.utampa.ui.CampusSafetyView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Cognito
        CognitoHelper.initCognito(this)
        AWSIAMCredentialsManager.appContext = this
        enableEdgeToEdge()
        setContent {
            UtampaTheme {
                val navController = rememberNavController()
                val buildings = loadBuildings(context = this)
                val dinings = loadDiningData(context = this)
                val parkings = loadParkingData(context = this)
                val recreationals = loadRecreationalData(context = this)

                var navKey by remember { mutableIntStateOf(0) } // Add this state variable

                Scaffold(
                    topBar = {
                        SignOutTopAppBar(authController = AuthController.getInstance(this))
                    },
                    bottomBar = {
                        //BottomNavBarApp(navController) // Call BottomNavBarApp here
                        var selectedItem by remember { mutableIntStateOf(0) }
                        val items = listOf("For You", "Campus", "Resources", "Profile")
                        val icons = listOf(
                            Icons.Filled.Favorite, // For you
                            Icons.Filled.Home,     // Campus
                            Icons.Filled.Info,     // Resources
                            Icons.Filled.Person    // Profile
                        )
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = { Icon(imageVector = icons[index], contentDescription = item) },
                                    label = { Text(item) },
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        when (item) {
                                            "For You" -> navController.navigate("for_you_screen")
                                            "Campus" -> navController.navigate("campus_home") // This is crucial
                                            "Resources" -> navController.navigate("resources_screen")
                                            "Profile" -> navController.navigate("profile_screen")
                                            // Add more cases as needed
                                        }
                                    }
                                )
                            }
                        }

                    },
                    content = { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Log.d("MainActivity", "****************navController hash: ${navController.hashCode()}")
                            NavHost(
                                navController = navController,
                                startDestination = "for_you_screen", //Kept start destination
                            ) {
                                composable("campus_home") { CampusScreen(navController) }
                                composable("building_list_screen") {
                                    BuildingListScreen(
                                        navController = navController,
                                        buildings = buildings,
                                        onBuildingClick = { buildingId ->
                                            navController.navigate("building_detail_screen/$buildingId")
                                        }
                                    )
                                }

                                composable("building_detail_screen/{buildingId}",
                                    arguments = listOf(navArgument("buildingId") { type = NavType.StringType })
                                ) { backStackEntry -> backStackEntry.arguments?.getString("buildingId")?.let { buildingId ->
                                    val building =  buildings.find { it.id == buildingId }?: buildings[0]
                                    BuildingDetailScreen(navController, building) {}
                                } }

                                composable("dining_list_screen") {
                                    DiningListScreen(
                                        navController = navController,
                                        dinings = dinings,
                                        ondiningClick = { diningId ->
                                            navController.navigate("dining_detail_screen/$diningId")
                                        }
                                    )
                                }

                                composable("dining_detail_screen/{diningId}",
                                    arguments = listOf(navArgument("diningId") { type = NavType.StringType })
                                ) { backStackEntry -> backStackEntry.arguments?.getString("diningId")?.let { diningId ->
                                    val dining =  dinings.find { it.id == diningId }?: dinings[0]
                                    DiningDetailScreen(navController, dining) {}
                                } }

                                composable("parking_screen") {
                                    ParkingListScreen(
                                        navController = navController,
                                        parkings = parkings, // Ensure you have a list of Parking objects loaded
                                        onparkingClick = { parkingId ->
                                            // Navigate to the parking detail screen with the selected parkingId
                                            navController.navigate("parking_detail_screen/$parkingId")
                                        }
                                    )
                                }

                                composable("parking_detail_screen/{parkingId}",
                                    arguments = listOf(navArgument("parkingId") { type = NavType.StringType })
                                ) { backStackEntry -> backStackEntry.arguments?.getString("parkingId")?.let { parkingId ->
                                    val parking =  parkings.find { it.id == parkingId }?: parkings[0]
                                    ParkingDetailScreen(navController, parking) {}
                                } }

                                composable("recreation_screen") {
                                    RecreationalListScreen(
                                        navController = navController,
                                        recreationals = recreationals,
                                        onRecreationalClick = { recreationalId ->
                                            // Navigate to the parking detail screen with the selected parkingId
                                            navController.navigate("recreational_detail_screen/$recreationalId")
                                        }
                                    )
                                }

                                composable("recreational_detail_screen/{recreationalId}",
                                    arguments = listOf(navArgument("recreationalId") { type = NavType.StringType })
                                ) { backStackEntry -> backStackEntry.arguments?.getString("recreationalId")?.let { recreationalId ->
                                    val recreational =  recreationals.find { it.id == recreationalId }?: recreationals[0]
                                    RecreationalDetailScreen(navController, recreational) {}
                                } }

                                // NEW: Campus Safety route added here
                                composable("campus_safety_screen") {
                                    CampusSafetyView(navController = navController)
                                }
                                composable("academic_success_center_screen") {
                                    AcademicSuccessCenterScreen(navController = navController)
                                }
                                composable("utampa_home") {
                                    UTampaApp(navController)
                                }

                                composable("for_you_screen") { ForYouScreen(navController = navController) } // Added ForYouScreen route
                                composable("resources_screen") { ResourcesScreen(navController = navController) } // Added
                                composable("profile_screen") { ProfileScreen(navController = navController) }     // Added

                            }
                        }
                    }
                )
            }
        }
    }


    // ✅ Fixed: Moved onNewIntent() OUTSIDE onCreate()
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent.data?.let { uri ->
            val uriString = uri.toString()
            Log.d("MainActivity", "Received redirect URI: $uriString") // Debug log

            // Extract all query parameters and log them
            val queryParams = uri.queryParameterNames
            for (param in queryParams) {
                Log.d("MainActivity", "Query param: $param = ${uri.getQueryParameter(param)}")
            }

            // Check if "state" exists
            val stateParam = uri.getQueryParameter("state")
            if (stateParam == "logout") {
                Log.d("MainActivity", "Ignoring logout redirect")
                return
            }

            // Otherwise, handle login normally
            AuthController.getInstance(this).handleOpenURL(uri)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignOutTopAppBar(
    authController: AuthController,
    title: String = "UTampa",
    onSignOut: () -> Unit = {}
) {
}


@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    val navController = rememberNavController() // ✅ Create a mock NavController


}
