package com.example.utampa.ui.theme.pages

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.utampa.R

@Composable
fun CampusSafetyScreen2() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Banner image
        Image(
            painter = painterResource(id = R.drawable.ic_spartan),
            contentDescription = "Campus Safety Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 10.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Campus Safety Office", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "The University of Tampa's Department of Campus Safety is dedicated to providing a safe and secure environment for the campus community.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Get direct access to Campus Safety in an on campus emergency or quickly connect to other services by downloading the new Spartan SOS app from the Apple App Store or Google Play. For more information on the Spartan SOS app, check out this intro video .",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "The University of Tampa considers the safety of all University community members integral to its educational mission. Officers patrol campus 24 hours a day, every day of the year. Campus Safety also assists with services such as publishing crime prevention information, providing pedestrian escorts, and vehicle and bicycle registration.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Contact us", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "For more information, contact the Department of Campus Safety at (813) 257-7777 or x7777 if on campus or email campussafety@ut.edu",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                val links = listOf(
                    "Safety Alert" to "https://www.ut.edu/about-ut/university-services/campus-safety/alert",
                    "File a Report" to "https://www.ut.edu/about-ut/university-services/campus-safety/file-a-report",
                    "Emergencies" to "https://www.ut.edu/about-ut/university-services/emergency",
                    "Vehicles Registration and Parking" to "https://www.ut.edu/about-ut/university-services/campus-safety/vehicle-registration-and-traffic-regulations",
                    "Campus Safety Services" to "https://www.ut.edu/about-ut/university-services/campus-safety/campus-programs",
                    "Safety Tips" to "https://www.ut.edu/about-ut/university-services/campus-safety/personal-safety-reminder"
                )

                links.forEach { (label, url) ->
                    Text(
                        text = label,
                        color = Color(red = 200, green = 16, blue = 46),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            }
                            .padding(vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
