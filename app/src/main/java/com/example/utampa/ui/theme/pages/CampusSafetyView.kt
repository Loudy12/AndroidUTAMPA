package com.example.utampa.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utampa.R
import androidx.navigation.NavController
import com.example.utampa.ui.theme.pages.navigateBackToCampus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusSafetyView(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navigateBackToCampus(navController) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back to Campus"
                        )
                    }
                },
                title = {
                    Text(
                        "Campus Safety Office",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.academicsuccessbackground),
                contentDescription = "Academic Success Center",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Campus Safety Office",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "The University of Tampa's Department of Campus Safety is dedicated to providing a safe and secure environment for the campus community.",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = "Get direct access to Campus Safety in an on-campus emergency or quickly connect to other services by downloading the new Spartan SOS app from the Apple App Store or Google Play. For more information on the Spartan SOS app, check out this intro video.",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "The University of Tampa considers the safety of all University community members integral to its educational mission. Officers patrol campus 24 hours a day, every day of the year. Campus Safety also assists with services such as publishing crime prevention information, providing pedestrian escorts, and vehicle and bicycle registration.",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Contact us",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "For more information, contact the Department of Campus Safety at (813) 257-7777 or x7777 if on campus or email campussafety@ut.edu",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Links
                LinkView("Safety Alert", "https://www.ut.edu/about-ut/university-services/campus-safety/alert", context)
                LinkView("File a Report", "https://www.ut.edu/about-ut/university-services/campus-safety/file-a-report", context)
                LinkView("Emergencies", "https://www.ut.edu/about-ut/university-services/emergency", context)
                LinkView("Vehicles Registration and Parking", "https://www.ut.edu/about-ut/university-services/campus-safety/vehicle-registration-and-traffic-regulations", context)
                LinkView("Campus Safety Services", "https://www.ut.edu/about-ut/university-services/campus-safety/campus-programs", context)
                LinkView("Safety Tips", "https://www.ut.edu/about-ut/university-services/campus-safety/personal-safety-reminder", context)
            }
        }
    }
}

// Reusable LinkView Composable
@Composable
fun LinkView(label: String, url: String, context: Context) {
    Text(
        text = label,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 16.sp,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
    )
}
