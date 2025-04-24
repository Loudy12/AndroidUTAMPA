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
fun HealthAndWellnessCenterScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Banner image
        Image(
            painter = painterResource(id = R.drawable.dickeyhealthwellness),
            contentDescription = "Medical Services Banner",
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

                Text("Medical Services", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Happy, Healthy Spartans", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Student Medical Services provides University of Tampa students with high-quality evidence-based medical care, behavioral health care, and health education to support collegiate success and sustained wellness.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "All full-time undergraduates (those students registered for 12 or more hours) and all full-time graduate students (those students registered for eight or more hours) are eligible to use the Health Center. Part-time students can use the health and counseling service for a fee. Student family members and faculty or staff members are not eligible for health and counseling services. New student health forms are required for all full-time, part-time, undergraduate, and graduate students.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("The Health Center staff, in partnership with Tampa General Hospital, provides health care for the following:",
                    style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(8.dp))

                val contextLinks1 = listOf(
                    "Acute illnesses and injuries" to "https://www.ut.edu/campus-life/student-services/dickey-health-and-wellness-center/treatment-prescriptions-and-testing",
                    "Immunizations" to "https://www.ut.edu/campus-life/student-services/dickey-health-and-wellness-center/student-immunization-requirements",
                    "Gynecology Services" to "https://www.ut.edu/campus-life/student-services/dickey-health-and-wellness-center/womens-health-services",
                    "Physicals" to "https://www.ut.edu/campus-life/student-services/dickey-health-and-wellness-center/medical-services",
                    "Allergy Injections" to "https://www.ut.edu/campus-life/student-services/dickey-health-and-wellness-center/allergy-clinic",
                    "Medical Infusion Space" to "https://www.ut.edu/campus-life/student-services/dickey-health-and-wellness-center/medical-services"
                )

                contextLinks1.forEach { (label, url) ->
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

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Most services are covered by students’ health insurance policy, so there are few out-of-pocket costs for full-time undergraduate and international students who have already paid the student health fee:",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                val contextLinks2 = listOf(
                    "Student Health Insurance Policy" to "https://www.ut.edu/campus-life/student-services/dickey-health-and-wellness-center/appointments-and-health-insurance",
                    "Student Health fee" to "https://www.ut.edu/campus-life/student-services/dickey-health-and-wellness-center/health-fee-faq"
                )

                contextLinks2.forEach { (label, url) ->
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

                Spacer(modifier = Modifier.height(24.dp))

                Text("Hours of Operation", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "The Health Center services students on an appointment-only basis, no walk-ins, please.",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "It is imperative that all individuals contact the Student Health Center at (813) 253-6250 to schedule an appointment before their visit to ensure that proper protective measures are taken.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text("Appointment only. No walk-ins, please.", style = MaterialTheme.typography.bodyMedium)
                Text("Medical Services Telephone: (813) 253-6250", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
