package com.example.utampa.ui.theme.guestview
//connect to Parking garages button
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.testing.TestNavHostController


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.utampa.R
import com.example.utampa.ui.theme.components.GuestParkingWidget
import com.example.utampa.ui.theme.components.ParkingGarageWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestParkingView(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Parking Garages",
                        fontSize = 24.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp), // Adds vertical spacing
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Widgets stacked with padding in between
            GuestParkingWidget()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Additional Parking Info", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Visit our campus parking page for more details.", fontSize = 14.sp, color = androidx.compose.ui.graphics.Color.Gray)
        }
    }
}



@Preview
@Composable
fun PreviewGuestParkingView() {
    val mockNavController = TestNavHostController(LocalContext.current)
    GuestParkingView(navController = mockNavController)
}
