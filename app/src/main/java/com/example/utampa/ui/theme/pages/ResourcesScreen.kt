package com.example.utampa.ui.theme.pages


import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.utampa.ui.theme.TampaRed


@Composable
fun ResourcesScreen(navController: NavController) {
    ResourcesScreenContent(navController = navController as NavHostController)
}



@Composable
fun ResourcesScreenContent(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = { Text("Resources") },
            backgroundColor = TampaRed,
            contentColor = MaterialTheme.colors.onPrimary,
            elevation = 4.dp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Resources") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Academics Section
            VerticalSectionWithWidgets(
                sectionTitle = "Academics",
                widgets = academicsWidgets,
                searchQuery = searchQuery.text,
                navController = navController
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Health and Safety Section
            VerticalSectionWithWidgets(
                sectionTitle = "Health and Safety",
                widgets = healthSafetyWidgets,
                searchQuery = searchQuery.text,
                navController = navController
            )
        }
    }
}

@Composable
fun VerticalSectionWithWidgets(
    sectionTitle: String,
    widgets: List<WidgetItem>,
    searchQuery: String,
    navController: NavHostController
) {
    var isSectionExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {

        // Section Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isSectionExpanded = !isSectionExpanded },
            elevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (isSectionExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isSectionExpanded) "Collapse" else "Expand"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isSectionExpanded) {
            val filteredWidgets = widgets.filter {
                it.title.contains(searchQuery, ignoreCase = true) || searchQuery.isEmpty()
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredWidgets) { widget ->
                    WidgetItemSquare(widget, navController)
                }
            }

            if (filteredWidgets.isEmpty() && searchQuery.isNotEmpty()) {
                Text(
                    text = "No results found",
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun WidgetItemSquare(widget: WidgetItem, navController: NavHostController?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable {
                when (widget.title) {
                    "Academic Success Center" -> navController?.navigate("academic_success_center2")
                    "Workday" -> navController?.navigate("workday_screen")
                    "Course Catalog" -> navController?.navigate("course_catalog_screen")
                    "Testing Center" -> navController?.navigate("testing_center_screen")
                    "Transcripts" -> navController?.navigate("transcripts_screen")
                    "Campus Health and Wellness" -> navController?.navigate("health_wellness_screen")
                    "Mental Health Counseling" -> navController?.navigate("mental_health_counseling_screen")
                    "Campus Safety" -> navController?.navigate("campus_safety_screen2")
                    "Emergency Contacts" -> navController?.navigate("emergency_contacts_screen")
                    "Wellness Programs" -> navController?.navigate("wellness_programs_screen")


                    // Add other navigations here for different widgets
                }
            },
        elevation = 4.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Icon(
                imageVector = widget.icon,
                contentDescription = widget.title,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = widget.title,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

//---------------------------------------------
//----------------- DATA ----------------------
//---------------------------------------------

data class WidgetItem(
    val title: String,
    val icon: ImageVector
)

val academicsWidgets = listOf(
    WidgetItem("Academic Success Center", Icons.Default.School),
    WidgetItem("Workday", Icons.Default.Work),
    WidgetItem("Course Catalog", Icons.Default.MenuBook),
    WidgetItem("Testing Center", Icons.Default.Assignment),
    WidgetItem("Transcripts", Icons.Default.Description)
)

val healthSafetyWidgets = listOf(
    WidgetItem("Campus Health and Wellness", Icons.Default.LocalHospital),
    WidgetItem("Mental Health Counseling", Icons.Default.Psychology),
    WidgetItem("Campus Safety", Icons.Default.Security),
    WidgetItem("Emergency Contacts", Icons.Default.Call),
    WidgetItem("Wellness Programs", Icons.Default.FitnessCenter)
)

//---------------------------------------------
//----------------- PREVIEWS ------------------
//---------------------------------------------







//@Composable
//fun ResourcesScreen() {
//    Text("Resources Screen", style = MaterialTheme.typography.headlineMedium)
//}
