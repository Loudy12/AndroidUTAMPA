package com.example.utampa

import java.net.URL

/**
 * A Kotlin equivalent of your Swift UserAttributesFetcher.
 * This object fetches the API URL dynamically from ConfigurationManager,
 * builds a signed GET request using AWSSignerHelper (with service name "execute-api"),
 * and performs the HTTP request.
 *
 * The credentials and configuration values are obtained directly from AuthController.
 */
object UserAttributesFetcher {

    /**
     * Fetches user attributes.
     *
     * @param completion a callback receiving (success: Boolean, response: String?).
     */
    fun fetchUserAttributes(completion: (Boolean, String?) -> Unit) {
        // Retrieve credentials and configuration directly from AuthController.
        // Replace 'MyApp.instance' with your global Application context.
        val authController = AuthController.getInstance(UtampaApplication.instance)
        val accessToken = authController.accessToken
        val accessKey = authController.awsAccessKey
        val secretKey = authController.awsSecretKey
        val sessionToken = authController.awsSessionToken
        val region = authController.region

        if (accessToken.isNullOrEmpty() ||
            accessKey.isNullOrEmpty() ||
            secretKey.isNullOrEmpty() ||
            sessionToken.isNullOrEmpty() ||
            region.isNullOrEmpty()) {
            completion(false, "Missing credentials in AuthController.")
            return
        }

        // Fetch the API URL dynamically from the configuration.
        ConfigurationManager.fetchParameterValue("/campus-app/apis/getUserAttributes") { result ->
            result.onSuccess { apiUrl ->
                println("API URL: $apiUrl")
                performFetchUserAttributes(apiUrl, accessToken, accessKey, secretKey, sessionToken, region, completion)
            }.onFailure { error ->
                completion(false, "Failed to fetch API URL: ${error.localizedMessage}")
            }
        }
    }

    private fun performFetchUserAttributes(
        apiUrl: String,
        accessToken: String,
        accessKey: String,
        secretKey: String,
        sessionToken: String,
        region: String,
        completion: (Boolean, String?) -> Unit
    ) {
        println("AAACCESS TOKEN: $accessToken")
        val fullURL = "$apiUrl?accessToken=$accessToken"
        val url = try {
            URL(fullURL)
        } catch (e: Exception) {
            completion(false, "Invalid URL for the endpoint.")
            return
        }

        // Create an instance of AWSSignerHelper with service name "execute-api".
        val signerHelper = AWSSignerHelper(
            accessKey = accessKey,
            secretKey = secretKey,
            serviceName = "execute-api",
            region = region
        )

        // Sign the GET request.
        val request = signerHelper.signRequest(url, HTTPMethod.GET, body = null, sessionToken = sessionToken)
        if (request == null) {
            completion(false, "Failed to create signed request.")
            return
        }

        // Perform the signed request using OkHttp.
        val client = okhttp3.OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                println("Error making request: ${e.localizedMessage}")
                completion(false, "Error making request: ${e.localizedMessage}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.use { resp ->
                    if (!resp.isSuccessful) {
                        completion(false, "HTTP error: ${resp.code}")
                        return
                    }
                    val responseData = resp.body?.string()
                    if (responseData == null) {
                        completion(false, "No data received from the server.")
                        return
                    }
                    println("API JSON Response: $responseData")
                    completion(true, responseData)
                }
            }
        })
    }
}
