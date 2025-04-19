package com.example.utampa.ui.theme.pages

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun AcademicSuccessCenterScreen(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        // Uses the shared navigation function to go back to Campus
                        navigateBackToCampus(navController)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back to Campus"
                        )
                    }
                },
                title = { Text("Academic Success Center", fontSize = 18.sp) },
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
        ) {
            // Background image
            Image(
                painter = painterResource(id = R.drawable.academicsuccessbackground),
                contentDescription = "Academic Success Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Content Box
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Academic Success Center",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "The Academic Success Center at The University of Tampa provides students with the tools they need to succeed academically. The Center houses the following academic support services:",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                SectionTitle("Academic Advising")
                LinkView(context, "Academic Petitions and Appeals", "https://www.ut.edu/academics/academic-support/academic-success-center/academic-advising-office/academic-petitions-and-appeals")
                LinkView(context, "Major Exploration", "https://www.ut.edu/academics/academic-support/academic-success-center/academic-advising-office/major-exploration")
                LinkView(context, "Pre-Health/Pre-Law Professions Advising", "https://www.ut.edu/academics/academic-support/academic-success-center/academic-advising-office/pre-health-pre-law-professional-advising")
                LinkView(context, "Request to Change Advisor or Major/Minor", "https://www.ut.edu/academics/academic-support/academic-success-center/academic-advising-office/request-to-change-advisor-or-major-minor-and-certificate")
                LinkView(context, "Transfer and Veteran Student Advising", "https://example.com/transfer-veteran")
                LinkView(context, "Withdrawal from the University", "https://www.ut.edu/academics/academic-support/academic-success-center/academic-advising-office/withdrawal-from-the-university")
                LinkView(context, "Withdrawing from Courses", "https://www.ut.edu/academics/academic-support/academic-success-center/academic-advising-office/withdrawal-from-the-university")

                SectionTitle("Student Accessibility and Academic Support")
                LinkView(context, "Academic Skills Courses", "https://ut.smartcatalogiq.com/en/current/catalog/course-descriptions/ask-academic-skills/")
                LinkView(context, "Coaching Student Success", "https://www.ut.edu/academics/academic-support/academic-success-center/student-accessibility-and-academic-support-/academic-coaching")
                LinkView(context, "Student Accessibility Services", "https://www.ut.edu/academics/academic-support/academic-success-center/student-accessibility-and-academic-support-/student-accessibility-services")
                LinkView(context, "Students Overcoming Academic Roadblocks (SOAR)", "https://www.ut.edu/academics/academic-support/academic-success-center/student-accessibility-and-academic-support-/students-overcoming-academic-roadblocks-(soar)")
                LinkView(context, "Testing Center", "https://www.ut.edu/academics/academic-support/academic-success-center/student-accessibility-and-academic-support-/student-accessibility-services/testing-center")
                LinkView(context, "Tutoring", "https://www.ut.edu/academics/academic-support/academic-success-center/student-accessibility-and-academic-support-/peer-tutoring")

                Text(
                    text = "The Academic Success Center is on the second floor of the Technology Building (TECH). You can reach us at (813) 257-5757 during business hours. Note that our summer hours are Monday through Thursday from 8 a.m to 5:30 p.m.",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

// Section Title Composable
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
    )
}

// LinkView Composable
@Composable
fun LinkView(context: Context, label: String, url: String) {
    Text(
        text = label,
        fontSize = 16.sp,
        color = Color.Red,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
    )
}
