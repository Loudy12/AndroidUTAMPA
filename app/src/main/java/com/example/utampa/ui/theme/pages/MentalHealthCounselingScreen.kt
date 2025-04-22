package com.example.utampa.ui.theme.pages

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.utampa.R

@Composable
fun MentalHealthCounselingScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header image
        Image(
            painter = painterResource(id = R.drawable.ic_spartan),
            contentDescription = "Mental Health Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        // Main content
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 10.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Counseling Services", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Healthy Minds, Healthy Spartans", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "The University of Tampa Medical and Counseling Services, located in the Dickey Health and Wellness Center directly behind Austin Hall, provides counseling to all full-time undergraduate students and all international graduate students. Domestic graduate students without an active student health insurance policy can be seen based on a fee for service; graduate student health insurance policies can be purchased through the United Healthcare Student Resources website at www.uhcsr.com.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Mental Health Support, After Hours Crisis and Emergency Care", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "The University of Tampa provides a 24/7 confidential student support line that is staffed by licensed professional counselors through a partnership with Christie Campus Health. Students that need in-the-moment support, after-hours crisis assistance, or emergency concerns should contact (833) 755-0484 for immediate support.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Concerned About a UT Student?", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Counseling Services understands there may be times a student becomes concerned about another student. The UT community is strongly encouraged to identify any students they are concerned about through the Spartan Support Program. The Spartan Support Program may be reached at (813) 257-3901, ext. 3901, or ssp@ut.edu. Spartan Support Program referral forms may also be accessed on the UT website.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Spartan Support Program Form",
                    color = Color(red = 200, green = 16, blue = 46),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.ut.edu/campus-life/student-services/student-care-and-advocacy/spartan-support-program")
                            )
                            context.startActivity(intent)
                        }
                        .padding(vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

