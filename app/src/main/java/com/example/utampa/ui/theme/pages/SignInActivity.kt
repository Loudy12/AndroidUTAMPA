package com.example.utampa.ui.theme.pages


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.utampa.ui.theme.UtampaTheme
import com.example.utampa.ui.theme.pages.SignInScreen
import com.example.utampa.AWS.AuthController

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authController = AuthController.getInstance(applicationContext)

        setContent {
            UtampaTheme {
                SignInScreen(authController)
            }
        }
    }
}
