package com.example.utampa.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.utampa.ui.theme.components.ClassCard

@Composable
fun CanvasWidget() {
    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(12.dp))

        Column {
            for (courseId in 1..4) {
                ClassCard(courseId = courseId)
            }
        }
    }
}

@Preview
@Composable
fun PreviewCanvasWidget() {
    CanvasWidget()
}
