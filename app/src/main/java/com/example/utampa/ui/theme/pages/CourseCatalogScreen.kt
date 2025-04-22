package com.example.utampa.ui.theme.pages

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.utampa.R

@Composable
fun CourseCatalogScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Background image (make sure to add R.drawable.academic_success_background in your resources)
        Image(
            painter = painterResource(id = R.drawable.ic_spartan),
            contentDescription = "Catalog Header",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        // Scrollable content with white card background
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
                    text = "University Catalogs",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "The 2024-2025 UT catalog is available online only. Questions should be directed to the Registrar's Office at registrar@ut.edu.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Current University Catalog",
                    style = MaterialTheme.typography.titleMedium
                )

                CatalogLink(label = "Course Catalog", url = "https://ut.smartcatalogiq.com/current/catalog/")

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Previous Years Catalogs",
                    style = MaterialTheme.typography.titleMedium
                )

                CatalogLink(label = "Previous Years", url = "https://www.ut.edu/academics/university-catalogs")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CatalogLink(label: String, url: String) {
    val context = LocalContext.current

    Text(
        text = label,
        color = Color(red = 200, green = 16, blue = 46),
        modifier = Modifier
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
            .padding(vertical = 8.dp),
        style = MaterialTheme.typography.bodyLarge
    )
}

