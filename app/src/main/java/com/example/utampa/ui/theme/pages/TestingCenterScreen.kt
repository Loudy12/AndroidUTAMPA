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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.utampa.R

@Composable
fun TestingCenterScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top banner image
        Image(
            painter = painterResource(id = R.drawable.ic_spartan),
            contentDescription = "Testing Center Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        // Scrollable white card
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
                    text = "Testing Center",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Within Student Accessibility Services, the Testing Center supports the university's efforts to ensure student success by administering exams and quizzes to students with approved exam/quiz accommodations. The Testing Center administers approximately 2,500 exams/quizzes per semester.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Under certain circumstances, students who do not receive accommodations as a part of Student Accessibility Services can take exams/quizzes through our Testing Center. The Testing Center also proctors Make-up Exam (non-SAS students), CLEP Exam (college level exam), Graduate Waiver Exam (COB students only), and ETS (educational testing services). Please see below for more information on how to request the correct exam.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "The Testing Center is located on the second floor of the Technology Building. Test takers can report to the Testing Center directly at the time of their scheduled exam. All exam requests that are not during the time of the original exam require instructor approval.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Testing Center Hours",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Hours",
                    color = Color(red = 200, green = 16, blue = 46),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.ut.edu/academics/academic-support/academic-success-center/student-accessibility-and-academic-support-/student-accessibility-services/testing-center")
                            )
                            context.startActivity(intent)
                        }
                        .padding(vertical = 8.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
