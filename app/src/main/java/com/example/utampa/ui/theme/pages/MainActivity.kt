package com.example.utampa.ui.theme.pages


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.utampa.AWS.AWSIAMCredentialsManager
import com.example.utampa.AWS.AuthController
import com.example.utampa.AWS.CognitoHelper
import com.example.utampa.UTampaApp
import com.example.utampa.ui.theme.UtampaTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Cognito
        CognitoHelper.initCognito(this)
        AWSIAMCredentialsManager.appContext = this
        enableEdgeToEdge()
        setContent {
            UtampaTheme {
                Scaffold(
                    topBar = {
                        SignOutTopAppBar(
                            authController = AuthController.getInstance(this)
                        )
                    },
                    content = { innerPadding ->
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)) {
                            UTampaApp()
                        }
                    }
                )
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent.data?.let { uri ->
            val uriString = uri.toString()
            Log.d("MainActivity", "Received redirect URI: $uriString") // Debug log

            // Extract all query parameters and log them
            val queryParams = uri.queryParameterNames
            for (param in queryParams) {
                Log.d("MainActivity", "Query param: $param = ${uri.getQueryParameter(param)}")
            }

            // Check if "state" exists
            val stateParam = uri.getQueryParameter("state")
            if (stateParam == "logout") {
                Log.d("MainActivity", "Ignoring logout redirect")
                return
            }

            // Otherwise, handle login normally
            AuthController.getInstance(this).handleOpenURL(uri)
        }
    }


}

@Composable
fun BottomNavBarApp() {
    var selectedItem by remember { mutableIntStateOf(0) }

    val items = listOf("For You", "Campus", "Resources", "Profile")
    val icons = listOf(
        Icons.Filled.Favorite,     // For you
        Icons.Filled.Home,  // Campus
        Icons.Filled.Info, // Resources
        Icons.Filled.Person    // Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (selectedItem) {
                0 -> ForYouScreen()
                1 -> CampusScreen()
                2 -> ResourcesScreen()
                3 -> ProfileScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignOutTopAppBar(
    authController: AuthController,
    title: String = "UTampa",
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit = {}
) {

}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    UtampaTheme {
        BottomNavBarApp()
    }
}
