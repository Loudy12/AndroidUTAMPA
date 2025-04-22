package com.example.utampa.ui.theme.pages

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utampa.R
import androidx.compose.foundation.clickable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusSafetyView() {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.ic_spartan),
                contentDescription = "Campus Safety Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content
            Text("Campus Safety Office", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "The University of Tampa's Department of Campus Safety is dedicated to providing a safe and secure environment for the campus community.",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Get direct access to Campus Safety in an on-campus emergency or quickly connect to other services by downloading the new Spartan SOS app from the Apple App Store or Google Play. For more information on the Spartan SOS app, check out this intro video.",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "The University of Tampa considers the safety of all University community members integral to its educational mission. Officers patrol campus 24 hours a day, every day of the year. Campus Safety also assists with services such as publishing crime prevention information, providing pedestrian escorts, and vehicle and bicycle registration.",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Contact Us Section
            Text("Contact us", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "For more information, contact the Department of Campus Safety at (813) 257-7777 or x7777 if on campus or email ",
                fontSize = 16.sp
            )
            LinkText("campussafety@ut.edu", "mailto:campussafety@ut.edu", context)

            Spacer(modifier = Modifier.height(16.dp))

            // Links
            LinkText("Safety Alert", "https://www.ut.edu/about-ut/university-services/campus-safety/alert", context)
            LinkText("File a Report", "https://www.ut.edu/about-ut/university-services/campus-safety/file-a-report", context)
            LinkText("Emergencies", "https://www.ut.edu/about-ut/university-services/emergency", context)
            LinkText("Vehicles Registration and Parking", "https://www.ut.edu/about-ut/university-services/campus-safety/vehicle-registration-and-traffic-regulations", context)
            LinkText("Campus Safety Services", "https://www.ut.edu/about-ut/university-services/campus-safety/campus-programs", context)
            LinkText("Safety Tips", "https://www.ut.edu/about-ut/university-services/campus-safety/personal-safety-reminder", context)
        }
    }
}

@Composable
fun LinkText(label: String, url: String, context: Context) {
    Text(
        text = label,
        fontSize = 16.sp,
        color = Color.Red,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(vertical = 4.dp).clickable {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    )
}
