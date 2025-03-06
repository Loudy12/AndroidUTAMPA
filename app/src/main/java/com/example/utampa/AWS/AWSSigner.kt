package com.example.utampa.AWS

import com.example.utampa.AWS.AWSSigner.SigningData.Companion.hashedPayload
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

// --- Helper Types ---

// HTTP method enum.
enum class HTTPMethod(val value: String) {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH")
}

// Body data as a sealed class.
sealed class BodyData {
    data class StringBody(val text: String) : BodyData()
    data class DataBody(val data: ByteArray) : BodyData()
    // Optionally add a ByteBuffer case if needed.
}

// Simple credentials class.
data class Credential(
    val accessKeyId: String,
    val secretAccessKey: String,
    val sessionToken: String? = null
)

// --- AWSSigner Class ---

class AWSSigner(
    val credentials: Credential,
    val serviceName: String,
    val region: String
) {

    companion object {
        // Compute the SHA256 hash of an empty array.
        val hashedEmptyBody: String = sha256Hex(ByteArray(0))

        // Create a timestamp formatter (UTC).
        private val timeStampDateFormatter: SimpleDateFormat = createTimeStampDateFormatter()

        private fun createTimeStampDateFormatter(): SimpleDateFormat {
            val formatter = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            return formatter
        }

        fun timestamp(date: Date): String = timeStampDateFormatter.format(date)

        // Compute SHA256 hash and return the hex digest.
        fun sha256Hex(data: ByteArray): String {
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(data)
            return digest.toHexString()
        }

        // Compute HMAC-SHA256 given a key and data.
        fun hmacSHA256(key: ByteArray, data: ByteArray): ByteArray {
            val algorithm = "HmacSHA256"
            val mac = Mac.getInstance(algorithm)
            mac.init(SecretKeySpec(key, algorithm))
            return mac.doFinal(data)
        }
    }

    /**
     * Signs the given headers and returns a new headers map with signature.
     */
    fun signHeaders(
        url: URL,
        method: HTTPMethod = HTTPMethod.GET,
        headers: Map<String, String> = mapOf(),
        body: BodyData? = null,
        date: Date = Date()
    ): Map<String, String> {
        val bodyHash = hashedPayload(body)
        val dateString = timestamp(date)

        // Build a mutable header map.
        val newHeaders = headers.toMutableMap()
        newHeaders["X-Amz-Date"] = dateString
        newHeaders["host"] = url.host ?: ""
        newHeaders["x-amz-content-sha256"] = bodyHash
        credentials.sessionToken?.let {
            newHeaders["x-amz-security-token"] = it
        }

        // Build signing data.
        val signingData = SigningData(
            url = url,
            method = method,
            headers = newHeaders,
            body = body,
            bodyHash = bodyHash,
            date = dateString,
            signer = this
        )

        // Compute the signature.
        val signature = signature(signingData)

        // Build Authorization header.
        val authorization = "AWS4-HMAC-SHA256 " +
                "Credential=${credentials.accessKeyId}/${signingData.date}/$region/$serviceName/aws4_request, " +
                "SignedHeaders=${signingData.signedHeaders}, " +
                "Signature=$signature"
        newHeaders["Authorization"] = authorization

        return newHeaders
    }

    /**
     * Generate a signed URL with query parameters for GET requests.
     */
    fun signURL(
        url: URL,
        method: HTTPMethod = HTTPMethod.GET,
        body: BodyData? = null,
        date: Date = Date(),
        expires: Int = 86400
    ): URL {
        // Use minimal headers containing host.
        val headers = mapOf("host" to (url.host ?: ""))
        val dateString = timestamp(date)
        var signingData = SigningData(
            url = url,
            method = method,
            headers = headers,
            body = body,
            date = dateString,
            signer = this
        )

        // Build query parameters.
        var query = url.query?.let { it + "&" } ?: ""
        query += "X-Amz-Algorithm=AWS4-HMAC-SHA256"
        query += "&X-Amz-Credential=${credentials.accessKeyId}/${signingData.date}/$region/$serviceName/aws4_request"
        query += "&X-Amz-Date=${signingData.datetime}"
        query += "&X-Amz-Expires=$expires"
        query += "&X-Amz-SignedHeaders=${signingData.signedHeaders}"
        credentials.sessionToken?.let {
            query += "&X-Amz-Security-Token=${URLEncoder.encode(it, StandardCharsets.UTF_8.name())}"
        }

        // Sort query parameters per AWS requirements.
        query = query.split("&").sorted().joinToString("&")

        // Update unsigned URL in signing data.
        val baseUrl = url.toString().substringBefore("?")
        signingData.unsignedURL = URL("$baseUrl?$query")

        // Append the signature.
        query += "&X-Amz-Signature=${signature(signingData)}"
        return URL("$baseUrl?$query")
    }

    // --- SigningData Data Class ---
    data class SigningData(
        val url: URL,
        val method: HTTPMethod,
        val hashedPayload: String,
        val datetime: String,
        val headersToSign: Map<String, String>,
        val signedHeaders: String,
        var unsignedURL: URL,
        val date: String,
        val signer: AWSSigner
    ) {
        constructor(
            url: URL,
            method: HTTPMethod = HTTPMethod.GET,
            headers: Map<String, String>,
            body: BodyData? = null,
            bodyHash: String? = null,
            date: String,
            signer: AWSSigner
        ) : this(
            url = if (url.path.isEmpty()) URL(url.toString() + "/") else url,
            method = method,
            hashedPayload = bodyHash ?: if (signer.serviceName == "s3") "UNSIGNED-PAYLOAD" else hashedPayload(body),
            datetime = date,
            headersToSign = headers.filterKeys { it.lowercase(Locale.getDefault()) != "authorization" }
                .mapKeys { it.key.lowercase(Locale.getDefault()) },
            signedHeaders = headers.keys
                .map { it.lowercase(Locale.getDefault()) }
                .filter { it != "authorization" }
                .sorted()
                .joinToString(";"),
            unsignedURL = url,
            date = date.take(8),
            signer = signer
        )

        companion object {
            fun hashedPayload(body: BodyData?): String {
                if (body == null) return hashedEmptyBody
                return when (body) {
                    is BodyData.StringBody -> sha256Hex(body.text.toByteArray(StandardCharsets.UTF_8))
                    is BodyData.DataBody -> sha256Hex(body.data)
                }
            }
        }
    }

    // --- Signature Calculation ---
    fun signature(signingData: SigningData): String {
        // Step 1: Create the derived signing key.
        val kDate = hmacSHA256("AWS4${credentials.secretAccessKey}".toByteArray(StandardCharsets.UTF_8),
            signingData.date.toByteArray(StandardCharsets.UTF_8))
        val kRegion = hmacSHA256(kDate, region.toByteArray(StandardCharsets.UTF_8))
        val kService = hmacSHA256(kRegion, serviceName.toByteArray(StandardCharsets.UTF_8))
        val kSigning = hmacSHA256(kService, "aws4_request".toByteArray(StandardCharsets.UTF_8))
        // Step 2: Create the string to sign.
        val stringToSign = stringToSign(signingData)
        // Step 3: Compute the signature.
        val signatureBytes = hmacSHA256(kSigning, stringToSign)
        return signatureBytes.toHexString()
    }

    fun stringToSign(signingData: SigningData): ByteArray {
        val canonicalRequestBytes = canonicalRequest(signingData)
        val hashedCanonicalRequest = sha256Hex(canonicalRequestBytes)
        val stringToSign = "AWS4-HMAC-SHA256\n" +
                signingData.datetime + "\n" +
                "${signingData.date}/$region/$serviceName/aws4_request\n" +
                hashedCanonicalRequest
        return stringToSign.toByteArray(StandardCharsets.UTF_8)
    }

    fun canonicalRequest(signingData: SigningData): ByteArray {
        val canonicalHeaders = signingData.headersToSign.entries
            .sortedBy { it.key }
            .joinToString("\n") { "${it.key}:${it.value.trim()}" }
        val canonicalQuery = signingData.unsignedURL.query ?: ""
        val canonicalRequest = "${signingData.method.value}\n" +
                "${signingData.unsignedURL.path.uriEncodeWithSlash()}\n" +
                "$canonicalQuery\n" +
                "$canonicalHeaders\n\n" +
                "${signingData.signedHeaders}\n" +
                signingData.hashedPayload
        return canonicalRequest.toByteArray(StandardCharsets.UTF_8)
    }
}

// --- Extension Functions ---

fun String.uriEncodeWithSlash(): String {
    // AWS requires that the slash character is not encoded in the canonical URI.
    val allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~/".toSet()
    return this.map { c ->
        if (allowed.contains(c)) c.toString() else String.format("%%%02X", c.code)
    }.joinToString("")
}

fun ByteArray.toHexString(): String = joinToString("") { "%02x".format(it) }
