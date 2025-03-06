package com.example.utampa.ui.theme.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utampa.AWS.AuthController
import com.example.utampa.findActivity

@Composable
fun SignInScreen(
    authController: AuthController = AuthController.getInstance(LocalContext.current)
) {
    // Capture LocalContext once
    val context = LocalContext.current

    // Local state variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localErrorMessage by remember { mutableStateOf<String?>(null) }
    var isPhoneNumberRequired by remember { mutableStateOf(false) }
    var isConfirmationStep by remember { mutableStateOf(false) }

    // Observe LiveData from authController
    val isLoading by authController.isLoading.observeAsState(initial = false)
    val authErrorMessage by authController.errorMessage.observeAsState(initial = null)
    val displayError = localErrorMessage ?: authErrorMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            LoadingView()
        } else {
            Text(text = "Sign Up / Log In", fontSize = 30.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    handleCognitoSignUp(authController, email, password) { result ->
                        if (result == "Success") {
                            checkPhoneNumberRequirement(authController) { required ->
                                isPhoneNumberRequired = required
                                if (required) {
                                    localErrorMessage = "Please enter your phone number for verification."
                                }
                            }
                        } else if (result == "ConfirmationRequired") {
                            isConfirmationStep = true
                            localErrorMessage = "An SMS confirmation code has been sent to your phone."
                        } else {
                            localErrorMessage = "Sign-Up failed: $result"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
            ) {
                Text(text = "Sign Up", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    handleCognitoLogin(authController, email, password) { result ->
                        if (result == "Success") {
                            checkPhoneNumberRequirement(authController) { required ->
                                isPhoneNumberRequired = required
                                if (required) {
                                    localErrorMessage = "Please enter your phone number for verification."
                                }
                            }
                        } else {
                            localErrorMessage = "Login failed: $result"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
            ) {
                Text(text = "Login", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    // Use the captured context to find the Activity
                    val activity = context.findActivity()
                    if (activity != null) {
                        authController.signInWithHostedUI(activity)
                    } else {
                        localErrorMessage = "Unable to retrieve Activity context."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "Globe",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign In with Okta", color = Color.White)
                }
            }
        }
        displayError?.let { msg ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = msg, color = Color.Red)
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

// --- Helper Functions ---

private fun handleCognitoSignUp(
    authController: AuthController,
    email: String,
    password: String,
    completion: (String) -> Unit
) {
    authController.signUp(email, password) { result ->
        completion(result)
    }
}

private fun handleCognitoLogin(
    authController: AuthController,
    email: String,
    password: String,
    completion: (String) -> Unit
) {
    authController.login(email, password) { result ->
        completion(result)
    }
}

private fun checkPhoneNumberRequirement(
    authController: AuthController,
    callback: (Boolean) -> Unit
) {
    authController.getUserAttributes { attributes, _ ->
        val required = attributes?.get("phone_number_verified") != "true"
        callback(required)
    }
}
