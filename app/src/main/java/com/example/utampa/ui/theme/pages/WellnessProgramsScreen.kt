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
fun WellnessProgramsScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header image
        Image(
            painter = painterResource(id = R.drawable.ic_spartan),
            contentDescription = "Wellness Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        // Scrollable content card
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
                    text = "Wellness Programs",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Rooms", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                Section("Wellness Room", "The Wellness Room offers a relaxing space for students to de-stress. During a visit, students can utilize the massage chairs, meditation pillow and resources, essential oils, and various de-stress materials.")

                Section("Snooze, or You Lose", "Snooze, or You Lose is a self-guided sleep hygiene improvement program. The 5-week program will cover; The Basics of Sleep, Napping, College Culture + Sleep, How to Build a Sleep Sanctuary, and Mindfulness and Sleep.")

                Section("De-Stress Kits", "De-stress kits can be rented out to help re-center, unwind, and have fun! Each item in our kit assists with mindfulness, physical entertainment, and personal self-care. Kits can be rented out to individual students, student groups, or classrooms.")

                Section("Nutrition Consultations", "The University of Tampa offers students free nutrition consultation services. If you are interested in meeting with a Certified Nutrition Specialist, please email Nutrition at nutrition@ut.edu.")

                Section("Spiritual Wellness", "UT works to further strengthen UT students’ academic and personal development through character-building, spiritual development, and enhancing their understanding of world cultures and religions.")

                Section("Vector Solutions", "Vector Solutions features online courses about alcohol and other drugs, sexual assault, and violence prevention that are used in schools nationwide as a requirement for new students.")

                Section("Alcohol and Other Drugs Program", "Institutional, local, and international addiction, tobacco cessation, and recovery organizations available to the UT community.")

                Section("Live Well UT", "Live Well UT is a student-led organization that empowers the UT community to continually improve healthy behaviors and lifestyles in themselves and others.")

                Section("Online Wellness Programming", "Enjoy a wide range of self-directed, video-based wellness options such as yoga, meditation, mindfulness, and more through the Uwill on-demand virtual platform. Create a username and access wellness programs from the left navigation menu.")

                Section("Resilience Reset Workshop", "How can you prepare to face the challenges that inevitably lie ahead this semester? Join us for an empowering workshop to learn about what resilience is and is not, practice mind-body skills, and explore intentional and game-changing perspectives. Resilience Reset teaches skills you can use immediately: mindfulness, disrupting negative thinking, gratitude, and self-compassion. All participants receive a participant workbook and a resilience toolkit with lots of free goodies.")

                Spacer(modifier = Modifier.height(12.dp))

                Text("For more information, please visit our website:", fontWeight = FontWeight.Bold)

                Text(
                    text = "Wellness Programs",
                    color = Color(red = 200, green = 16, blue = 46),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.ut.edu/campus-life/student-services/wellness-services/wellness-programs#Snooze")
                            )
                            context.startActivity(intent)
                        }
                        .padding(vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun Section(title: String, body: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    Text(text = body, style = MaterialTheme.typography.bodyMedium)
}
