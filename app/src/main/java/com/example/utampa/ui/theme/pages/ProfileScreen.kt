package com.example.utampa.ui.theme.pages

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.utampa.R
import com.example.utampa.data.User
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    var user by remember { mutableStateOf(User()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Image Picker Launcher
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            profileImageUri = uri
        }

    // Simulate Fetching User Data
    LaunchedEffect(Unit) {
        delay(2500) // Simulate network delay
        user = User(
            name = "John Doe",
            email = "john.doe@example.com",
            profileImageUrl = null
        )
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", style = MaterialTheme.typography.headlineMedium) },
                actions = {
                    IconButton(onClick = { Toast.makeText(context, "Notifications", Toast.LENGTH_SHORT).show() }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_bell),
                            contentDescription = "Notifications",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF5F5F5)), // Light gray background
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))


                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(profileImageUri),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(180.dp)
                                    .clip(CircleShape) //circle stays circle on upload
                                    .background(Color.Gray, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {

                            Box(
                                modifier = Modifier
                                    .size(180.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Upload", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(12.dp))


                    if (isLoading) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Fetching user data...", fontSize = 14.sp, color = Color.Gray)
                    } else {
                        Text(user.name.ifEmpty { "Name" }, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(user.email.ifEmpty { "example@domain.com" }, fontSize = 16.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(20.dp))


                    Image(
                        painter = painterResource(id = R.drawable.ic_spartan),
                        contentDescription = "Spartan ID Card",
                        modifier = Modifier
                            .width(360.dp)
                            .height(180.dp)
                            .background(Color.White, RoundedCornerShape(15.dp))
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Details Section
                    DetailCard(title = "Meal Swipes", value = "7 meal swipes remaining")
                    DetailCard(title = "Spartan Dollars", value = "$150.00")
                    DetailCard(title = "Class Year", value = "Senior")
                    DetailCard(title = "Major", value = "Computer Science")

                    Spacer(modifier = Modifier.height(20.dp))

                    // Profile Actions
                    ProfileButton(title = "Saved", icon = android.R.drawable.ic_menu_save) { Toast.makeText(context, "Saved Clicked", Toast.LENGTH_SHORT).show() }
                    ProfileButton(title = "Clubs and Groups", icon = android.R.drawable.ic_menu_myplaces) { Toast.makeText(context, "Clubs Clicked", Toast.LENGTH_SHORT).show() }
                    ProfileButton(title = "Settings", icon = android.R.drawable.ic_menu_preferences) { Toast.makeText(context, "Settings Clicked", Toast.LENGTH_SHORT).show() }
                }
            }
        }
    )
}

@Composable
fun DetailCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(value, color = Color.Gray)
        }
    }
}

@Composable
fun ProfileButton(title: String, icon: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)) // Custom Red
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
