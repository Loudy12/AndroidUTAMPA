package com.example.utampa.ui.theme.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.utampa.R
import com.example.utampa.ui.theme.TampaRed

@Composable
fun ParkingPassWidget(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Keeps background transparent
        modifier = Modifier
            .size(180.dp, 254.dp) // Ensure it remains the same size
            .padding(8.dp),
        contentPadding = PaddingValues(0.dp) // Removes default button padding to prevent layout shifting
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            // Overlay main title here above glassy
            Text(
                text = "Parking Garages",
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp,
                lineHeight = 34.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(8.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_paleparkingp),
                contentDescription = "Parking Icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(23.dp)
                    .size(60.dp)
            )

            Box(
                modifier = Modifier
                    .size(180.dp, 254.dp)
                    .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(12.dp)) // Glassy effect
                    .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .blur(20.dp) // Causes the blur
                    .graphicsLayer(alpha = 1.4f)
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Throw in random words behind the glass to give it aesthetic
                    Text(
                        text = "Title Here",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp,
                        color = TampaRed
                    )

                    Column {
                        Text(
                            text = "SPARTANS SPARTANS",
                            fontWeight = FontWeight.Bold,
                            color = TampaRed,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "RAAAAAAAAAAAAAAAA",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            letterSpacing = 1.sp,
                            color = TampaRed
                        )
                        Row {
                            Text(
                                text = "GO SPARTANS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Light,
                                color = TampaRed
                            )
                            Text(
                                text = "UTAMPA!",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = TampaRed,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


