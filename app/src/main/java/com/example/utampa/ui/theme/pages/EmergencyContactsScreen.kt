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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.utampa.R

@Composable
fun EmergencyContactsScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header image
        Image(
            painter = painterResource(id = R.drawable.ic_spartan),
            contentDescription = "Emergency Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        // Main content card
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 10.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Emergency Contacts",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "If you have an emergency requiring police, fire, or ambulance service response, call 911. Then, reach Campus Safety at (813) 257-7777.",
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Important Phone Numbers",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                val phoneNumbers = listOf(
                    "Department of Campus Safety: ext. 7777, off-campus, (813) 257-7777",
                    "Emergencies Only: 911",
                    "Tampa Fire Department: (813) 223-4211",
                    "Tampa General Hospital: (813) 251-7000",
                    "Tampa Police Department: (813) 231-6130"
                )

                phoneNumbers.forEach {
                    Text(text = it, style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "If an unexpected, time-sensitive emergency occurs (e.g., fire, accident), it should be reported immediately to the Department of Campus Safety. Officers are on duty 24 hours a day, seven days a week throughout the year.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                val links = listOf(
                    "Tropical Weather Alerts" to "https://www.ut.edu/about-ut/university-services/emergency/tropical-weather-alerts",
                    "Medical Attention" to "https://www.ut.edu/about-ut/university-services/campus-safety/automated-external-defibrillators",
                    "Active Shooter" to "https://www.ut.edu/about-ut/university-services/emergency/active-shooter",
                    "Fire" to "https://www.ut.edu/about-ut/university-services/emergency/fire",
                    "Chemical Spill" to "https://www.ut.edu/about-ut/university-services/emergency/chemical-spill",
                    "Violence and Threats" to "https://www.ut.edu/about-ut/university-services/emergency/violence-and-threats",
                    "Bomb Threats and Suspicious Packages" to "https://www.ut.edu/about-ut/university-services/emergency/bomb-threats-and-suspicious-packages",
                    "Counseling Services" to "https://www.ut.edu/campus-life/student-services/dickey-health-and-wellness-center/counseling-services",
                    "Utility Outage" to "https://www.ut.edu/about-ut/university-services/emergency/utility-outage"
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
