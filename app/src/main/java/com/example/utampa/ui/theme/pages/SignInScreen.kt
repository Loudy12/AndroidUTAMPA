package com.example.utampa.ui.theme.pages

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner


import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utampa.AWS.AuthController
import com.example.utampa.ui.theme.pages.GuestActivity
import com.example.utampa.ui.theme.pages.MainActivity
import com.example.utampa.R
import com.example.utampa.findActivity
import com.example.utampa.ui.theme.TampaRed
import com.example.utampa.ui.theme.darkBackground

@Composable
fun SignInScreen(
    authController: AuthController = AuthController.getInstance(LocalContext.current)
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val lifecycleOwner = LocalLifecycleOwner.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localErrorMessage by remember { mutableStateOf<String?>(null) }
    var isPhoneNumberRequired by remember { mutableStateOf(false) }
    var isConfirmationStep by remember { mutableStateOf(false) }

    val isLoading by authController.isLoading.observeAsState(initial = false)
    val authErrorMessage by authController.errorMessage.observeAsState(initial = null)
    val displayError = localErrorMessage ?: authErrorMessage

    if (isLoading) {
        LoadingView()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF262626))
                .verticalScroll(rememberScrollState()) //can delete along with imports after testing
                .padding(horizontal = 3.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Banner Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(45.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.henryplant),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )
                Image(
                    painter = painterResource(id = R.drawable.utlogo),
                    contentDescription = "Spartan Icon",
                    modifier = Modifier
                        .height(130.dp)
                        .align(Alignment.Center)
                        .padding(top = 60.dp)
                )
            }


            Spacer(modifier = Modifier.height(18.dp))

            Text("UTampa App", fontSize = 30.sp, color = Color.White, modifier = Modifier.padding(top = 12.dp))
            Text(
                "Explore More Reasons to Love UT With an App on Your Smartphone.",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 25.dp)

            )

            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    if (activity != null) {
                        authController.signInWithHostedUI(activity)

                        authController.isLoggedIn.observe(lifecycleOwner) { loggedIn ->
                            if (loggedIn == true) {
                                context.startActivity(Intent(context, MainActivity::class.java))
                                activity.finish()
                            }
                        }
                    } else {
                        localErrorMessage = "Unable to retrieve Activity context."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                elevation = ButtonDefaults.elevation(14.dp)
            ) {
                Text("Continue with Okta", color = Color.White, fontSize = 16.sp)
            }


            Spacer(modifier = Modifier.height(25.dp))

            OutlinedButton(
                onClick = {
                    context.startActivity(Intent(context, GuestActivity::class.java))
                    activity?.finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Continue as Guest", fontSize = 16.sp, color = darkBackground)
            }

            displayError?.let { msg ->
                Text(
                    text = msg,
                    color = Color.Red,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Terms of Service | Privacy Policy",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 40.dp)
            )
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
