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
fun TranscriptsScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top image (ic_spartan)
        Image(
            painter = painterResource(id = R.drawable.pool),
            contentDescription = "Transcripts Banner",
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
                    text = "Transcripts Ordering and Information",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Both official and unofficial transcripts are available. Printed official transcripts contain the University seal, are on special tamper-proof paper and are sealed in an envelope that states it is only official if received unopened. Electronic PDF delivery of transcripts is available when ordering online. Unofficial transcripts are free and contain the same text but are printed on plain paper.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Ordering Transcripts",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "For more information on how to order your transcripts, visit the UTampa website.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Ordering Transcripts",
                    color = Color(red = 200, green = 16, blue = 46),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.ut.edu/about-ut/university-services/office-of-the-registrar/the-university-of-tampa-transcript-ordering-information")
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
