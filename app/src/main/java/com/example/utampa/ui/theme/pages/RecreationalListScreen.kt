package com.example.utampa.ui.theme.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import com.example.utampa.models.Recreational
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecreationalListScreen(
    recreationals: List<Recreational>,
    onRecreationalClick: (String) -> Unit
) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }

    val filteredRecreationals = if (searchText.length >= 2) {
        recreationals.filter { it.name.contains(searchText, ignoreCase = true) }
    } else {
        recreationals
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Recreation Spots", style = MaterialTheme.typography.titleLarge) },
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
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search recreational spots...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(filteredRecreationals) { recreational ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onRecreationalClick(recreational.id) }
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
                        Text(text = recreational.name, style = MaterialTheme.typography.titleLarge)
                        Text(text = recreational.type, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
