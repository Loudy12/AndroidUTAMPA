package com.example.utampa.ui.theme.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import com.example.utampa.models.Parking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkingListScreen(
    parkings: List<Parking>,
    onParkingClick: (String) -> Unit
) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }

    val filteredParkings = if (searchText.length >= 2) {
        parkings.filter { it.name.contains(searchText, ignoreCase = true) }
    } else {
        parkings
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Parking Garages", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search parking garages...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(filteredParkings) { parking -> // ✅ This should now work properly
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onParkingClick(parking.id) }
                    ) {
                        Image(
                            painter = painterResource(id = parking.getImageResId(context)),
                            contentDescription = parking.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = parking.name, style = MaterialTheme.typography.titleLarge)
                        Text(text = parking.type, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
