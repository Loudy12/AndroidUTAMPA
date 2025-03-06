package com.example.utampa

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import androidx.compose.runtime.livedata.observeAsState
import android.util.Log
import com.example.utampa.AWS.AuthController
import com.example.utampa.AWS.UserAttributesFetcher
import com.example.utampa.ui.theme.pages.BottomNavBarApp
import com.example.utampa.ui.theme.pages.LoadingView
import com.example.utampa.ui.theme.pages.SignInScreen

@Composable
fun UTampaApp(
    authController: AuthController = AuthController.getInstance(LocalContext.current)
) {
    // Local state to simulate a loading screen for at least 2 seconds.
    var isLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(2000)
        isLoaded = true
    }

    if (!isLoaded) {
        LoadingView()
    } else {
        // Observe the logged-in state from AuthController.
        val isLoggedIn by authController.isLoggedIn.observeAsState(initial = false)
        if (!isLoggedIn) {
            // Show the sign-in screen if the user is not logged in.
            SignInScreen(authController = authController)
        } else {
            // Wait until the credentials have been loaded.
            LaunchedEffect(authController) {
                // Poll every 500ms until all required credentials are non-null and non-empty.
                while (authController.accessToken.isNullOrEmpty() ||
                    authController.awsAccessKey.isNullOrEmpty() ||
                    authController.awsSecretKey.isNullOrEmpty() ||
                    authController.awsSessionToken.isNullOrEmpty() ||
                    authController.region.isNullOrEmpty()
                ) {
                    delay(500)
                    Log.d("UTampaApp", "waiting for keys")
                }
                // Once credentials are available, fetch user attributes.
                UserAttributesFetcher.fetchUserAttributes { success, response ->
                    if (success) {
                        Log.d("UTampaApp", "User attributes: $response")
                    } else {
                        Log.e("UTampaApp", "Error fetching user attributes: $response")
                    }
                }
            }
            // Display your main UI.
            BottomNavBarApp()
        }
    }
}
