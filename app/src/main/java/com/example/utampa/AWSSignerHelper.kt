package com.example.utampa

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import java.net.URL

/**
 * AWSSignerHelper wraps an AWSSigner to sign an HTTP request.
 * The signing is done using AWS Signature Version 4.
 *
 * @param accessKey The AWS access key.
 * @param secretKey The AWS secret key.
 * @param serviceName The name of the AWS service (e.g. "execute-api").
 * @param region The AWS region (e.g. "us-east-2").
 */
class AWSSignerHelper(
    accessKey: String,
    secretKey: String,
    private val serviceName: String,
    private val region: String
) {
    // Store the credentials (without sessionToken).
    private val credentials = Credential(accessKey, secretKey)

    /**
     * Signs the given HTTP request and returns a signed OkHttp Request.
     *
     * @param url The endpoint URL.
     * @param method The HTTP method.
     * @param body Optional request body as a ByteArray.
     * @param sessionToken The AWS session token.
     * @return A signed OkHttp Request.
     */
    fun signRequest(url: URL, method: HTTPMethod, body: ByteArray?, sessionToken: String): Request? {
        // Create an instance of AWSSigner using the credentials and provided session token.
        val signer = AWSSigner(
            credentials = Credential(
                accessKeyId = credentials.accessKeyId,
                secretAccessKey = credentials.secretAccessKey,
                sessionToken = sessionToken
            ),
            serviceName = serviceName,
            region = region
        )

        // Prepare headers for signing.
        val headers = mutableMapOf<String, String>()
        headers["Content-Type"] = "application/json"
        headers["x-amz-security-token"] = sessionToken

        // Convert the body (if provided) into a BodyData instance.
        val bodyData = body?.let { BodyData.DataBody(it) }

        // Sign the headers using AWSSigner.
        val signedHeaders = signer.signHeaders(url, method, headers, bodyData)

        // Build an OkHttp Request with the given URL and method.
        val requestBody = body?.let { RequestBody.create("application/json".toMediaType(), it) }
        val requestBuilder = Request.Builder()
            .url(url)
            .method(method.value, requestBody)

        // Set the signed headers to the request.
        for ((key, value) in signedHeaders) {
            requestBuilder.addHeader(key, value)
        }

        return requestBuilder.build()
    }
}
