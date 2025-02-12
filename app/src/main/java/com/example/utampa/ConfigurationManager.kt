package com.example.utampa
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

object ConfigurationManager {

    private val cachedParameters: MutableMap<String, String> = mutableMapOf()
    private const val bootstrapAPIEndpoint = "https://mo9ytate1a.execute-api.us-east-2.amazonaws.com/prod/bootstrap"
    private val httpClient = OkHttpClient()

    // Fetch a single parameter by name.
    fun fetchParameterValue(parameterName: String, completion: (Result<String>) -> Unit) {
        fetchMultipleParameterValues(listOf(parameterName)) { result ->
            result.onSuccess { parameters ->
                parameters[parameterName]?.let {
                    completion(Result.success(it))
                } ?: completion(
                    Result.failure(Exception("Parameter value not found."))
                )
            }.onFailure { error ->
                completion(Result.failure(error))
            }
        }
    }

    // Fetch multiple parameter values.
    fun fetchMultipleParameterValues(parameterNames: List<String>, completion: (Result<Map<String, String>>) -> Unit) {
        // If all parameters are cached, return them immediately.
        if (parameterNames.all { cachedParameters.containsKey(it) }) {
            val result = parameterNames.associateWith { cachedParameters[it]!! }
            completion(Result.success(result))
            return
        }
        // Build URL with comma-separated parameter names.
        val parameterNamesString = parameterNames.joinToString(separator = ",")
        val url = "$bootstrapAPIEndpoint?parameterNames=$parameterNamesString"

        val request = Request.Builder().url(url).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ConfigurationManager", "Error fetching parameters: ${e.localizedMessage}")
                completion(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                response.use { resp ->
                    if (!resp.isSuccessful) {
                        completion(Result.failure(Exception("HTTP error: ${resp.code}")))
                        return
                    }
                    val body = resp.body?.string()
                    if (body == null) {
                        completion(Result.failure(Exception("No data received from bootstrap API.")))
                        return
                    }
                    try {
                        val jsonResponse = JSONObject(body)
                        val parametersJson = jsonResponse.getJSONObject("parameters")
                        val parameters = mutableMapOf<String, String>()
                        parametersJson.keys().forEach { key ->
                            val value = parametersJson.getString(key)
                            parameters[key] = value
                            cachedParameters[key] = value // Cache the value.
                        }
                        completion(Result.success(parameters))
                    } catch (e: Exception) {
                        completion(Result.failure(e))
                    }
                }
            }
        })
    }
}
