package com.example.utampa.ui.theme.pages

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.utampa.ui.theme.UtampaTheme
import com.example.utampa.ui.theme.pages.SignInScreen
import com.example.utampa.AWS.AuthController
import androidx.activity.compose.setContent

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authController = AuthController.getInstance(applicationContext)

        setContent {
            UtampaTheme {
                SignInScreen(authController)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent.data?.let { uri ->
            val uriString = uri.toString()
            Log.d("SignInActivity", "Received redirect URI: $uriString")

            val queryParams = uri.queryParameterNames
            for (param in queryParams) {
                Log.d("SignInActivity", "Query param: $param = ${uri.getQueryParameter(param)}")
            }

            val stateParam = uri.getQueryParameter("state")
            if (stateParam == "logout") {
                Log.d("SignInActivity", "Ignoring logout redirect")
                return
            }

            AuthController.getInstance(this).handleOpenURL(uri)
        }
    }

}
