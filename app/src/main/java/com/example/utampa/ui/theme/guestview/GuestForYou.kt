package com.example.utampa.ui.theme.guestview

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import com.example.utampa.R
import androidx.compose.material3.ButtonDefaults
import com.example.utampa.ui.theme.TampaRed



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestForYou(navController: NavController) {
    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(title = { Text("Welcome to The University of Tampa!") }) //center this and change text
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TourSection(context)
            Spacer(modifier = Modifier.height(16.dp))
            ParkingSection(navController, context) //needs nav
            Spacer(modifier = Modifier.height(16.dp))
            AroundTampaBaySection(navController) // needs nav
            Spacer(modifier = Modifier.height(16.dp))
            StudentsFavoriteSpotsWidget()
            Spacer(modifier = Modifier.height(16.dp))
            VisitTipsFAQWidget()
        }
    }
}

@Composable
fun TourSection(context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(12.dp))
            .padding(16.dp),

    ) {
        Text("Schedule a Tour", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        TourButton("Undergraduate Visit") {
            openUrl(context, "https://apply-undg.ut.edu/portal/officevisit") //link working
        }
        TourButton("Graduate Visit") {
            openUrl(context, "https://www.ut.edu/graduate-degrees/graduate-campus-visit") // link working
        }
        TourButton("Transfer Visit") {
            openUrl(context, "https://www.ut.edu/continuing-studies/continuing-studies-admissions-calendar") // link working
        }
    }
}

@Composable
fun ParkingSection(navController: NavController, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text("Parking", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Button(
                onClick = { navController.navigate("parkingList") },
                colors = ButtonDefaults.buttonColors(containerColor = TampaRed) // Apply TampaRed
            ) {
                Text("Parking Garages")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    openUrl(
                        context,
                        "https://www.ut.edu/about-ut/university-services/campus-safety/visitor-pass-form"
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = TampaRed) // Apply TampaRed
            ) {
                Text("Visitor Parking Pass", fontSize = 13.sp)
            }
        }
    }
    }

@Composable
fun AroundTampaBaySection(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(12.dp))
            .clickable { navController.navigate("aroundTampaBay") }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Around Tampa Bay", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Explore the area", fontSize = 14.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(painterResource(id = R.drawable.arrowforward), contentDescription = "Arrow")
        }
    }
}

@Composable
fun TourButton(title: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = TampaRed)

    ) {
        Text(title)
    }
    Spacer(modifier = Modifier.height(8.dp))
}



fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

@Composable
fun VisitTipsFAQWidget() {
    val faqs = listOf(
        "Where can I park?" to "Visitor parking is available in the West Parking Garage.",
        "Are campus tours available?" to "Yes! Guided tours are offered every weekday.",
        "Can I access the library?" to "Visitors can access the library with a visitor pass.",
        "What facilities can visitors use?" to "Visitors are welcome in public areas on campus."
    )

    var expandedIndex by remember { mutableStateOf(-1) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Frequently Asked Questions", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        faqs.forEachIndexed { index, faq ->
            Column(modifier = Modifier.clickable { expandedIndex = if (expandedIndex == index) -1 else index }) {
                Text(faq.first, fontWeight = FontWeight.Bold)
                if (expandedIndex == index) {
                    Text(faq.second, color = Color.Gray)
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun StudentsFavoriteSpotsWidget() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Student's Favorite Spots", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        val spots = listOf(
            "Plant Hall" to "Historic landmark and architectural gem.",
            "Fitness Center" to "Hub of campus life with dining and study lounges.",
            "Sykes Chapel" to "Peaceful retreat for reflection and performances.",
            "The Riverwalk" to "Enjoy scenic views along the Hillsborough River.",
            "Riseman Aquatic Center" to "Showcasing artwork by students and faculty."
        )
        spots.forEach { spot ->
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(spot.first, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Text(spot.second, color = Color.Gray)
            }
        }
    }
}

@Preview
@Composable
fun PreviewGuestForYou() {
    val mockNavController = TestNavHostController(LocalContext.current)
    GuestForYou(navController = mockNavController)
}
