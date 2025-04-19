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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.utampa.models.Recreational


@Composable
fun RecreationalListBackArrow(navController: NavController) {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back to Campus"
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun RecreationalListScreen(
    navController: NavController,
    recreationals: List<Recreational>,
    onRecreationalClick: (String) -> Unit
) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }
    val filteredrecreationals = if (searchText.length >= 2) {
        recreationals.filter { it.name.contains(searchText, ignoreCase = true) }
    } else {
        recreationals
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "recreationals",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    RecreationalListBackArrow(navController)
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
                label = { Text("Search recreations...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(filteredrecreationals) { recreational ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                if (navController.currentDestination?.route != "recreational_detail_screen") {
                                    onRecreationalClick(recreational.id)
                                }
                            }
                    ) {
                        Image(
                            painter = painterResource(id = recreational.getImageResId(context)),
                            contentDescription = recreational.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = recreational.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = recreational.type,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
