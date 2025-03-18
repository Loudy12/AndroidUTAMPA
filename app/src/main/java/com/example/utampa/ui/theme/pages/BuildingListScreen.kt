package com.example.utampa.ui.theme.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.utampa.data.Building

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildingListScreen(
    buildings: List<Building>,
    onBuildingClick: (String) -> Unit
) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }

    // Only filter if the search text has 2 or more characters; otherwise, show full list.
    val filteredBuildings = if (searchText.length >= 2) {
        buildings.filter { it.name.contains(searchText, ignoreCase = true) }
    } else {
        buildings
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text ="Buildings",
                        style = MaterialTheme.typography.titleLarge)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                )

            )
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Box
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search buildings...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(filteredBuildings) { building ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onBuildingClick(building.id) }
                    ) {
                        // Full-width image
                        Image(
                            painter = painterResource(id = building.getImageResId(context)),
                            contentDescription = building.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Building Name (bold, larger)
                        Text(
                            text = building.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        // Building Type (normal, smaller)
                        Text(
                            text = building.type,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}