package com.example.utampa
import android.content.Context
import android.util.Log
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions

object AWSIAMCredentialsManager {

    // In Android the credentials provider needs a Context.
    // Initialize this once in your Application class.
    lateinit var appContext: Context

    // Map to store logins.
    private val userLogins: MutableMap<String, String> = mutableMapOf()

    /**
     * Fetch AWS IAM credentials using the provided idToken.
     *
     * @param idToken The Cognito ID token.
     * @param regionString The AWS region as a string.
     * @param identityPoolId The Cognito Identity Pool ID.
     * @param userPoolId The Cognito User Pool ID.
     * @param completion Callback with AWSCredentials on success or null on failure.
     */
    fun fetchAWSCredentials(
        idToken: String,
        regionString: String,
        identityPoolId: String,
        userPoolId: String,
        completion: (AWSCredentials?) -> Unit
    ) {
        // Set up the logins map.
        userLogins["cognito-idp.$regionString.amazonaws.com/$userPoolId"] = idToken
        Log.d("AWSIAMCredentialsManager", "Logins: $userLogins")

        // Create the credentials provider.
        val credentialsProvider = CognitoCachingCredentialsProvider(
            appContext,
            identityPoolId,
            Regions.fromName(regionString)
        )
        // Set the logins map.
        credentialsProvider.logins = userLogins

        // Refresh the credentials provider to get new credentials.
        try {
            credentialsProvider.refresh()
            val credentials = credentialsProvider.credentials
            completion(credentials)
        } catch (e: Exception) {
            Log.e("AWSIAMCredentialsManager", "Failed to get AWS credentials: ${e.localizedMessage}")
            completion(null)
        }
    }
}
