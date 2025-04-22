package com.example.utampa.ui.theme.pages

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.utampa.R
import com.example.utampa.ui.theme.TampaRed

@Composable
fun AcademicSuccessCenterScreen2(navController: NavHostController? = null) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Academic Success Center") },
                navigationIcon = {
                    navController?.let {
                        IconButton(onClick = { it.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                backgroundColor = TampaRed, // <- Use your custom red here
                contentColor = MaterialTheme.colors.onPrimary
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            //  Image section at the top
            Image(
                painter = painterResource(id = R.drawable.academicsuccessbackground),
                contentDescription = "Spartan Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Welcome to the Academic Success Center!",
                    style = MaterialTheme.typography.h6
                )

                Spacer(modifier = Modifier.height(12.dp))

                val annotatedText = buildAnnotatedString {
                    append("For more information, visit the ")
                    pushStringAnnotation(tag = "URL", annotation = "https://www.google.com/")
                    withStyle(
                        style = SpanStyle(
                            color = TampaRed,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Academic Success Center Website")
                    }
                    pop()
                    append(".")
                }

                Text(
                    text = annotatedText,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = """
                    The Academic Success Center at The University of Tampa provides students with the tools they need to succeed academically.

                    The Academic Success Center is on the second floor of the Technology Building (TECH). You can reach us at (813) 257-5757 during business hours. Note that our summer hours are Monday through Thursday from 8 a.m to 5:30 p.m.

                    The Academic Success Center provides comprehensive services that position all students to achieve academic success. We facilitate the high-quality educational experience of The University of Tampa through the Academic Advising Office and Office of Student Accessibility and Academic Support.
                    """.trimIndent(),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

