package com.example.utampa.ui.theme.pages
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.utampa.data.Building
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack


@Composable
fun BuildingListBackArrow(navController: NavController) {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back to Campus"
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun BuildingListScreen(
    navController: NavController,
    buildings: List<Building>,
    onBuildingClick: (String) -> Unit
) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }
    val filteredBuildings = if (searchText.length >= 2) {
        buildings.filter { it.name.contains(searchText, ignoreCase = true) }
    } else {
        buildings
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Buildings",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    BuildingListBackArrow(navController)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
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
                            .clickable {
                                if (navController.currentDestination?.route != "building_detail_screen") {
                                    onBuildingClick(building.id)
                                }
                            }
                    ) {
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
                        Text(
                            text = building.name,
                            style = MaterialTheme.typography.titleLarge
                        )
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
