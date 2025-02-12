package com.example.utampa.ui.theme.components

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utampa.R // Import your generated R file
import com.example.utampa.data.Assignment
import kotlinx.coroutines.delay

@Composable
fun ClassCard(courseId: Int) {
    var isExpanded by remember { mutableStateOf(false) }
    var assignments by remember { mutableStateOf<List<Assignment>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // Fetch assignments only when expanded for the first time
    LaunchedEffect(isExpanded) {
        if (isExpanded && assignments.isEmpty()) {
            isLoading = true
            assignments = fetchAssignments(courseId)
            isLoading = false
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header: Class Title & Expand Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bookmark), // ✅ Uses drawable
                    contentDescription = "Class Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Class $courseId",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { isExpanded = !isExpanded }
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Expand Button"
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Assignment List (Collapsible)
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    when {
                        isLoading -> Text("Loading assignments...", fontSize = 14.sp, color = Color.Gray)
                        assignments.isEmpty() -> Text("No assignments available.", fontSize = 14.sp, color = Color.Gray)
                        else -> assignments.forEach { assignment ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(assignment.name, fontSize = 14.sp)
                                Text("Due: ${assignment.formattedDueDate}", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}


// Simulated API Call (Delay to Mimic Fetching)
private suspend fun fetchAssignments(courseId: Int): List<Assignment> {
    delay(1500) // Simulate network delay
    return listOf(
        Assignment(1, "Project Report", "2024-03-15T23:59:00Z"),
        Assignment(2, "Homework 3", "2024-03-18T23:59:00Z"),
        Assignment(3, "Midterm Exam", null)
    )
}

