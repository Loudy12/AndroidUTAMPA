package com.example.utampa
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.MutableLiveData
import com.amazonaws.auth.AWSSessionCredentials
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler
import com.amazonaws.regions.Regions
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import android.content.Intent


import com.example.utampa.AWSIAMCredentialsManager  // Ensure this path matches your project structure
import com.example.utampa.AWSIAMCredentialsManager.appContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class AuthController private constructor(private val context: Context) {

    // LiveData properties for UI observation
    val isLoading = MutableLiveData(true)
    val isLoggedIn = MutableLiveData(false)
    val errorMessage = MutableLiveData<String?>()

    // Tokens
    var idToken: String? = null
    var accessToken: String? = null
    var refreshToken: String? = null
    var tokenExpirationDate: Date? = null

    // AWS IAM Credentials
    var awsAccessKey: String? = null
    var awsSecretKey: String? = null
    var awsSessionToken: String? = null

    // PKCE properties
    private var state: String? = null
    private var codeVerifier: String? = null

    // Cognito configuration variables
    private var clientId: String? = null
    private var userPoolId: String? = null
    private var identityPoolId: String? = null
    var region: String? = null
    private var cognitoDomain: String? = null
    private var oauthScopes: List<String> = emptyList()
    private var callbackUrls: List<String> = emptyList()

    // AWS Cognito User Pool – created after configuration is fetched.
    lateinit var userPool: CognitoUserPool

    // SharedPreferences for token storage
    private val prefs: SharedPreferences =
        context.getSharedPreferences("AuthControllerPrefs", Context.MODE_PRIVATE)

    // OkHttpClient for network calls
    private val httpClient = OkHttpClient()

    init {
        // When the controller is created, fetch configuration and load any saved tokens.
        fetchCognitoConfiguration {
            loadTokensFromStorage()
        }
    }

    // MARK: - Fetch Cognito Configuration from SSM Parameter Store
    fun fetchCognitoConfiguration(completion: () -> Unit) {
        val parameterNames = listOf(
            "/campus-app/cognito/cognito-client-id",
            "/campus-app/cognito/cognito-user-pool-id",
            "/campus-app/cognito/cognito-region",
            "/campus-app/cognito/cognito-user-pool-domain",
            "/campus-app/cognito/cognito-oauth-scopes",
            "/campus-app/cognito/cognito-callback-urls",
            "/campus-app/cognito/cognito-identity-pool-id"
        )

        ConfigurationManager.fetchMultipleParameterValues(parameterNames) { result ->
            result.onSuccess { parameters ->
                clientId = parameters["/campus-app/cognito/cognito-client-id"]
                userPoolId = parameters["/campus-app/cognito/cognito-user-pool-id"]
                region = parameters["/campus-app/cognito/cognito-region"]
                cognitoDomain = parameters["/campus-app/cognito/cognito-user-pool-domain"]
                identityPoolId = parameters["/campus-app/cognito/cognito-identity-pool-id"]

                parameters["/campus-app/cognito/cognito-oauth-scopes"]?.let {
                    oauthScopes = it.split(",").map { scope -> scope.trim() }
                }
                parameters["/campus-app/cognito/cognito-callback-urls"]?.let {
                    callbackUrls = it.split(",").map { url -> url.trim() }
                }

                Log.d("AuthController", "Fetched clientId: $clientId")
                Log.d("AuthController", "Fetched userPoolId: $userPoolId")
                Log.d("AuthController", "Fetched region: $region")
                Log.d("AuthController", "Fetched cognitoDomain: $cognitoDomain")
                Log.d("AuthController", "Fetched identityPoolId: $identityPoolId")
                Log.d("AuthController", "Fetched oauthScopes: $oauthScopes")
                Log.d("AuthController", "Fetched callbackUrls: $callbackUrls")

                registerCognitoUserPool()
            }.onFailure { error ->
                errorMessage.postValue("Error fetching Cognito configuration: ${error.localizedMessage}")
            }
            isLoading.postValue(false)
            completion()
        }
    }

    // MARK: - Register Cognito User Pool
    private fun registerCognitoUserPool() {
        if (region == null || clientId == null || userPoolId == null) {
            errorMessage.postValue("Incomplete Cognito configuration. Please check parameter store values.")
            return
        }
        // Create the CognitoUserPool instance.
        userPool = CognitoUserPool(
            context,
            userPoolId,
            clientId,
            null,
            Regions.fromName(region)
        )
        Log.d("AuthController", "Cognito User Pool registered.")
    }

    // MARK: - Sign-In with Hosted UI (using PKCE and Custom Tabs)
    fun signInWithHostedUI(activity: Activity) {
        state = UUID.randomUUID().toString()
        codeVerifier = generateCodeVerifier()

        if (codeVerifier == null || state == null) {
            errorMessage.postValue("Failed to generate code verifier")
            return
        }

        if (clientId == null || region == null || cognitoDomain == null) {
            errorMessage.postValue("Missing configuration values for Cognito")
            Log.e("AuthController", "Missing configuration: clientId=$clientId, region=$region, cognitoDomain=$cognitoDomain")
            return
        }

        val codeChallenge = codeChallengeFor(codeVerifier!!)
        val redirectUri = if (callbackUrls.isNotEmpty()) callbackUrls.first() else "myapp://callback"
        val callbackScheme = Uri.parse(redirectUri).scheme ?: run {
            errorMessage.postValue("Invalid redirect URI")
            return
        }
        val scope = oauthScopes.joinToString(separator = "+")
        val responseType = "code"
        val codeChallengeMethod = "S256"

        val authURLString = "https://$cognitoDomain.auth.$region.amazoncognito.com/oauth2/authorize" +
                "?response_type=$responseType" +
                "&client_id=$clientId" +
                "&redirect_uri=${Uri.encode(redirectUri)}" +
                "&scope=$scope" +
                "&state=$state" +
                "&code_challenge_method=$codeChallengeMethod" +
                "&code_challenge=$codeChallenge" +
                "&prompt=login" // 👈 Forces a login screen every time
        Log.d("AuthController", "Generated authURLString: $authURLString")

        val authUri = Uri.parse(authURLString)
        // Launch the URL in a Custom Tab.
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(activity, authUri)
        // When your app receives the deep link (via your intent-filter), call handleOpenURL(uri)
    }

    // MARK: - Handle Redirect URL
    fun handleOpenURL(uri: Uri) {
        Log.d("AuthController", "handleOpenURL called with URI: $uri")
        val redirectUri = "myapp://callback" // Must match your redirect URI
        if (codeVerifier == null || state == null) {
            errorMessage.postValue("Invalid state or code verifier")
            return
        }
        val returnedState = uri.getQueryParameter("state")
        Log.d("AuthController", "Returned state: $returnedState, expected state: $state")
        if (returnedState != state) {
            errorMessage.postValue("State mismatch")
            return
        }
        uri.getQueryParameter("error_description")?.let { errorDesc ->
            errorMessage.postValue("Authentication error: $errorDesc")
            Log.e("AuthController", "Authentication error from redirect: $errorDesc")
            return
        }
        val code = uri.getQueryParameter("code")
        Log.d("AuthController", "Authorization code received: $code")
        if (code != null) {
            exchangeCodeForTokens(code, codeVerifier!!)
        } else {
            errorMessage.postValue("No authorization code found")
        }
    }

    // MARK: - Exchange Code for Tokens
    private fun exchangeCodeForTokens(code: String, codeVerifier: String) {
        if (region == null || cognitoDomain == null || clientId == null) {
            errorMessage.postValue("Missing configuration values for token exchange.")
            Log.e("AuthController", "Configuration missing: region=$region, cognitoDomain=$cognitoDomain, clientId=$clientId")
            return
        }
        val tokenURLString = "https://$cognitoDomain.auth.$region.amazoncognito.com/oauth2/token"
        Log.d("AuthController", "Token exchange URL: $tokenURLString")
        val tokenUri = Uri.parse(tokenURLString)
        val redirectUri = "myapp://callback" // Must match your registered callback URI

        // Build the POST request body.
        val formBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", clientId!!)
            .add("code_verifier", codeVerifier)
            .add("code", code)
            .add("redirect_uri", redirectUri)
            .build()

        val request = Request.Builder()
            .url(tokenUri.toString())
            .post(formBody)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorMessage.postValue("Token exchange error: ${e.localizedMessage}")
                Log.e("AuthController", "Token exchange error", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use { resp ->
                    if (!resp.isSuccessful) {
                        errorMessage.postValue("Token exchange failed: HTTP ${resp.code}")
                        Log.e("AuthController", "Token exchange failed with HTTP code ${resp.code}")
                        return
                    }
                    val bodyString = resp.body?.string()
                    Log.d("AuthController", "Token endpoint response body: $bodyString")
                    if (bodyString == null) {
                        errorMessage.postValue("No data received from token endpoint")
                        Log.e("AuthController", "Empty token response")
                        return
                    }
                    try {
                        val tokenResponse = JSONObject(bodyString)
                        Log.d("AuthController", "Parsed token response: $tokenResponse")
                        accessToken = tokenResponse.optString("access_token", null)
                        idToken = tokenResponse.optString("id_token", null)
                        refreshToken = tokenResponse.optString("refresh_token", null)
                        tokenResponse.optLong("expires_in", 0L).takeIf { it > 0 }?.let { expiresIn ->
                            tokenExpirationDate = Date(System.currentTimeMillis() + expiresIn * 1000)
                            saveToken("expires_in", tokenExpirationDate!!.toISOString())
                            Log.d("AuthController", "Token expires at: ${tokenExpirationDate!!.toISOString()}")
                        } ?: Log.d("AuthController", "expires_in not provided")

                        accessToken?.let { saveToken("access_token", it) }
                        idToken?.let { saveToken("id_token", it) }
                        refreshToken?.let { saveToken("refresh_token", it) }

                        // (Optional) Log the token values – be careful with sensitive data.
                        Log.d("AuthController", "Access token: $accessToken")
                        Log.d("AuthController", "ID token: $idToken")
                        Log.d("AuthController", "Refresh token: $refreshToken")

                        isLoggedIn.postValue(true)
                        afterSuccessfulLogin()
                    } catch (e: Exception) {
                        errorMessage.postValue("Token parsing error: ${e.localizedMessage}")
                        Log.e("AuthController", "Error parsing token response", e)
                    }
                }
            }
        })
    }

    // MARK: - PKCE Helper Methods
    private fun generateCodeVerifier(): String? {
        return try {
            val randomBytes = ByteArray(32)
            SecureRandom().nextBytes(randomBytes)
            randomBytes.toBase64UrlEncodedString()
        } catch (e: Exception) {
            Log.e("AuthController", "Error generating code verifier", e)
            null
        }
    }

    private fun codeChallengeFor(verifier: String): String {
        val bytes = verifier.toByteArray(StandardCharsets.US_ASCII)
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return digest.toBase64UrlEncodedString()
    }

    fun signOut() {
        if (cognitoDomain == null || clientId == null || callbackUrls.isEmpty()) {
            Log.e("AuthController", "Cognito configuration not loaded yet!")
            return
        }

        deleteToken("access_token")
        deleteToken("id_token")
        deleteToken("refresh_token")
        deleteToken("expires_in")

        // Construct the logout URL with a state parameter
        val logoutUri = Uri.encode(callbackUrls.first()) // Use the first callback URL
        val revokeUrl = "https://$cognitoDomain.auth.$region.amazoncognito.com/logout" +
                "?client_id=$clientId" +
                "&redirect_uri=$logoutUri" +
                "&response_type=code" +
                "&state=logout" // 👈 Add this

        Log.d("AuthController", "Logout URL: $revokeUrl")

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(revokeUrl))
        appContext.startActivity(intent)

        accessToken = null
        idToken = null
        refreshToken = null
        isLoggedIn.postValue(false)
        Log.d("AuthController", "User signed out.")
    }



    // MARK: - Sign Up
    fun signUp(email: String, password: String, completion: (String) -> Unit) {
        val userAttributes = CognitoUserAttributes().apply {
            addAttribute("email", email)
        }
        userPool.signUpInBackground(email, password, userAttributes, null, object : SignUpHandler {
            override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                Log.d("AuthController", "User signed up.")
                completion("Success")
            }

            override fun onFailure(exception: Exception) {
                val msg = exception.localizedMessage ?: "Unknown error during sign up"
                Log.e("AuthController", "Sign-up error: $msg")
                completion("Error: $msg")
            }
        })
    }
    // MARK: - Confirm Sign-Up (Verify Email)
    fun verifyEmail(confirmationCode: String, email: String, completion: (String) -> Unit) {
        val user = userPool.getUser(email)
        user.confirmSignUpInBackground(confirmationCode, false, object : GenericHandler {
            override fun onSuccess() {
                Log.d("AuthController", "User confirmed successfully.")
                completion("Success")
            }

            override fun onFailure(exception: Exception?) {
                val msg = exception?.localizedMessage ?: "Unknown error during confirmation"
                Log.e("AuthController", "Confirmation error: $msg")
                completion("Error: $msg")
            }
        })
    }

    // MARK: - Login
    fun login(email: String, password: String, completion: (String) -> Unit) {
        val user = userPool.getUser(email)
        user.getSessionInBackground(object : AuthenticationHandler {
            override fun onSuccess(
                userSession: com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession?,
                newDevice: com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice?
            ) {
                Log.d("AuthController", "User signed in.")
                isLoggedIn.postValue(true)
                accessToken = userSession?.accessToken?.jwtToken
                idToken = userSession?.idToken?.jwtToken
                refreshToken = userSession?.refreshToken?.token

                accessToken?.let { saveToken("access_token", it) }
                idToken?.let { saveToken("id_token", it) }
                refreshToken?.let { saveToken("refresh_token", it) }

                // Fetch AWS IAM credentials
                if (idToken != null && region != null && identityPoolId != null && userPoolId != null) {
                    AWSIAMCredentialsManager.fetchAWSCredentials(
                        idToken = idToken!!,
                        regionString = region!!,
                        identityPoolId = identityPoolId!!,
                        userPoolId = userPoolId!!
                    ) { credentials ->
                        if (credentials != null) {
                            Log.d("AuthController", "AWS IAM credentials fetched successfully.")
                        } else {
                            Log.e("AuthController", "Failed to fetch AWS IAM credentials.")
                        }
                    }
                } else {
                    Log.e("AuthController", "Missing required configuration values for AWS IAM credentials.")
                }
            }

            override fun getAuthenticationDetails(
                authenticationContinuation: com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation?,
                userId: String?
            ) {
                val authenticationDetails = com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails(
                    email, password, null
                )
                authenticationContinuation?.setAuthenticationDetails(authenticationDetails)
                authenticationContinuation?.continueTask()
            }

            override fun getMFACode(continuation: com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation?) {
                // Handle MFA if needed.
            }

            override fun authenticationChallenge(continuation: com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation?) {
                // Handle other challenges if required.
            }

            override fun onFailure(exception: Exception?) {
                val msg = exception?.localizedMessage ?: "Unknown sign-in error"
                Log.e("AuthController", "Sign-in error: $msg")
                completion("Error: $msg")
            }
        })
    }

    // MARK: - Get User Attributes
    fun getUserAttributes(completion: (Map<String, String>?, Exception?) -> Unit) {
        if (accessToken == null) {
            Log.e("AuthController", "No access token available, user not logged in")
            completion(null, Exception("User is not logged in"))
            return
        }
        val user = userPool.currentUser
        user.getDetailsInBackground(object : GetDetailsHandler {
            override fun onSuccess(result: com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails?) {
                val attributesMap = mutableMapOf<String, String>()
                result?.attributes?.attributes?.forEach { (key, value) ->
                    Log.d("AuthController", "Attribute: $key = $value")
                    attributesMap[key] = value
                }
                completion(attributesMap, null)
            }

            override fun onFailure(exception: Exception?) {
                Log.e("AuthController", "Error getting user attributes: ${exception?.localizedMessage}")
                completion(null, exception)
            }
        })
    }

    // MARK: - Token Storage Helpers
    private fun saveToken(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    private fun getToken(key: String): String? = prefs.getString(key, null)

    private fun deleteToken(key: String) {
        prefs.edit().remove(key).apply()
    }

    // MARK: - Load Tokens from Storage
    private fun loadTokensFromStorage() {
        accessToken = getToken("access_token")
        idToken = getToken("id_token")
        refreshToken = getToken("refresh_token")
        getToken("expires_in")?.let {
            tokenExpirationDate = parseIso8601(it)
        }
        if (!accessToken.isNullOrEmpty() && !idToken.isNullOrEmpty()) {
            checkTokenExpiration { needsRefresh ->
                if (needsRefresh) {
                    refreshAccessToken { success ->
                        if (success) {
                            isLoggedIn.postValue(true)
                            Log.d("AuthController", "Access token refreshed successfully.")
                            afterSuccessfulLogin()
                        } else {
                            Log.e("AuthController", "Failed to refresh access token.")
                        }
                    }
                } else {
                    isLoggedIn.postValue(true)
                    Log.d("AuthController", "User is already logged in with valid tokens.")
                    afterSuccessfulLogin()
                }
            }
        } else {
            isLoggedIn.postValue(false)
            Log.d("AuthController", "No saved tokens found. User needs to log in.")
        }
    }

    private fun afterSuccessfulLogin() {
        if (idToken != null && region != null && identityPoolId != null && userPoolId != null) {
            AWSIAMCredentialsManager.fetchAWSCredentials(
                idToken = idToken!!,
                regionString = region!!,
                identityPoolId = identityPoolId!!,
                userPoolId = userPoolId!!
            ) { credentials ->
                if (credentials != null) {
                    Log.d("AuthController", "AWS IAM credentials fetched successfully.")
                    // Assign the fetched credentials to the AuthController fields.
                    // Use the getter methods to retrieve the values.
                    awsAccessKey = credentials.getAWSAccessKeyId()
                    awsSecretKey = credentials.getAWSSecretKey()
                    // If the credentials also implement AWSSessionCredentials, get the session token.
                    if (credentials is AWSSessionCredentials) {
                        awsSessionToken = credentials.getSessionToken()
                    }
                } else {
                    Log.e("AuthController", "Failed to fetch AWS IAM credentials.")
                }
            }
        } else {
            Log.e("AuthController", "Missing required configuration values for AWS IAM credentials.")
        }
    }

    // MARK: - Check Token Expiration
    fun checkTokenExpiration(completion: (Boolean) -> Unit) {
        if (tokenExpirationDate == null) {
            completion(true)
            return
        }
        val now = Date()
        if (now >= tokenExpirationDate) {
            Log.d("AuthController", "Access token has expired.")
            completion(true)
        } else {
            Log.d("AuthController", "Access token is still valid.")
            completion(false)
        }
    }

    // MARK: - Refresh Access Token
    fun refreshAccessToken(completion: (Boolean) -> Unit) {
        val refreshTok = refreshToken
        if (refreshTok.isNullOrEmpty()) {
            Log.e("AuthController", "No refresh token available.")
            completion(false)
            return
        }
        if (cognitoDomain == null || region == null || clientId == null) {
            Log.e("AuthController", "Missing Cognito configuration for token refresh.")
            completion(false)
            return
        }
        val tokenURLString = "https://$cognitoDomain.auth.$region.amazoncognito.com/oauth2/token"
        Log.d("AuthController", "Token refresh URL: $tokenURLString")
        val formBody = FormBody.Builder()
            .add("grant_type", "refresh_token")
            .add("client_id", clientId!!)
            .add("refresh_token", refreshTok)
            .build()
        val request = Request.Builder()
            .url(tokenURLString)
            .post(formBody)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("AuthController", "Error refreshing token: ${e.localizedMessage}", e)
                completion(false)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use { resp ->
                    if (!resp.isSuccessful) {
                        Log.e("AuthController", "Failed to refresh token. HTTP ${resp.code}")
                        completion(false)
                        return
                    }
                    val bodyString = resp.body?.string()
                    if (bodyString == null) {
                        Log.e("AuthController", "Empty token refresh response")
                        completion(false)
                        return
                    }
                    try {
                        val tokenResponse = JSONObject(bodyString)
                        accessToken = tokenResponse.optString("access_token", null)
                        idToken = tokenResponse.optString("id_token", null)
                        tokenResponse.optLong("expires_in", 0L).takeIf { it > 0 }?.let { expiresIn ->
                            tokenExpirationDate = Date(System.currentTimeMillis() + expiresIn * 1000)
                            saveToken("expires_in", tokenExpirationDate!!.toISOString())
                        }
                        accessToken?.let { saveToken("access_token", it) }
                        idToken?.let { saveToken("id_token", it) }
                        completion(true)
                    } catch (e: Exception) {
                        Log.e("AuthController", "Error parsing refresh token response: ${e.localizedMessage}", e)
                        completion(false)
                    }
                }
            }
        })
    }

    // Extension function to convert Date to an ISO8601 string.
    private fun Date.toISOString(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(this)
    }

    // Companion object for converting an ISO8601 string back to a Date.
    companion object {
        // Singleton instance
        @Volatile
        private var instance: AuthController? = null
        fun getInstance(context: Context): AuthController =
            instance ?: synchronized(this) {
                instance ?: AuthController(context.applicationContext).also { instance = it }
            }
    }
}

// Extension function for Base64 URL encoding (no padding).
fun ByteArray.toBase64UrlEncodedString(): String {
    return Base64.encodeToString(this, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        .trimEnd('=')
}

fun parseIso8601(isoString: String): Date? {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        formatter.parse(isoString)
    } catch (e: Exception) {
        null
    }
}
