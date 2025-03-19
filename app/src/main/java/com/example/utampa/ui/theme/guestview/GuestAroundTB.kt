package com.example.utampa.ui.theme.guestview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.utampa.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.utampa.ui.theme.TampaRed

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestAroundTB() {
    val events = remember { mutableStateListOf<Ticket>() }

    // fake api req
    LaunchedEffect(Unit) {
        fetchFakeTicketmasterEvents(events)
    }

    GuestAroundTBContent(events)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestAroundTBContent(events: List<Ticket>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Around Tampa Bay",
                        fontSize = 24.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Navigate Back */ }) {
                        Icon(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (events.isEmpty()) {
                CircularProgressIndicator() // ✅ Show loading until data is available
            } else {
                events.forEach { event ->
                    TicketCard(event = event)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun TicketCard(event: Ticket) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = event.image),
                contentDescription = "Event Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = event.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Date: ${event.startDateTime}",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = "Location: ${event.location.city}, ${event.location.state} - ${event.location.venue}",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.url))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = TampaRed)
        ) {
            Text("More Info")
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
suspend fun fetchFakeTicketmasterEvents(events: MutableList<Ticket>) {
    delay(1000)

    val fakeEvents = listOf(
        Ticket(
            name = "Tampa Music Festival",
            startDateTime = "2025-04-15",
            location = TicketLocation("Tampa", "FL", "Amalie Arena"),
            image = "https://source.unsplash.com/300x200/?concert", //fake images
            url = "https://www.tampafestival.com"
        ),
        Ticket(
            name = "Gasparilla Pirate Festival",
            startDateTime = "2025-01-25",
            location = TicketLocation("Tampa", "FL", "Downtown Tampa"),
            image = "https://source.unsplash.com/300x200/?pirate",
            url = "https://www.gasparillapiratefest.com"
        ),
        Ticket(
            name = "Tampa Bay Lightning Game",
            startDateTime = "2025-03-10",
            location = TicketLocation("Tampa", "FL", "Amalie Arena"),
            image = "https://source.unsplash.com/300x200/?hockey",
            url = "https://www.nhl.com/lightning"
        )
    )

    events.clear()
    events.addAll(fakeEvents)
}

// ✅ Mock Data Models (No real API needed)
data class Ticket(
    val name: String,
    val startDateTime: String,
    val location: TicketLocation,
    val image: String,
    val url: String
)

data class TicketLocation(
    val city: String,
    val state: String,
    val venue: String
)


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewGuestAroundTB() {
    val previewEvents = remember { mutableStateListOf<Ticket>() }


    LaunchedEffect(Unit) {
        previewEvents.clear()
        previewEvents.addAll(
            listOf(
                Ticket(
                    name = "Tampa Music Festival",
                    startDateTime = "2025-04-15",
                    location = TicketLocation("Tampa", "FL", "Amalie Arena"),
                    image = "https://source.unsplash.com/300x200/?concert",
                    url = "https://www.tampafestival.com"
                ),
                Ticket(
                    name = "Gasparilla Pirate Festival",
                    startDateTime = "2025-01-25",
                    location = TicketLocation("Tampa", "FL", "Downtown Tampa"),
                    image = "https://source.unsplash.com/300x200/?pirate",
                    url = "https://www.gasparillapiratefest.com"
                ),
                Ticket(
                    name = "Tampa Bay Lightning Game",
                    startDateTime = "2025-03-10",
                    location = TicketLocation("Tampa", "FL", "Amalie Arena"),
                    image = "https://source.unsplash.com/300x200/?hockey",
                    url = "https://www.nhl.com/lightning"
                )
            )
        )
    }

    GuestAroundTBContent(previewEvents)
}
