package ua.monobank.acquiring.internal

import org.slf4j.LoggerFactory
import ua.monobank.acquiring.MonobankAcquiringConfig
import ua.monobank.acquiring.exception.MonobankApiException
import ua.monobank.acquiring.exception.MonobankAuthException
import ua.monobank.acquiring.exception.MonobankNetworkException
import ua.monobank.acquiring.exception.MonobankRateLimitException
import ua.monobank.acquiring.model.common.ErrorResponse
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Duration

/**
 * Internal blocking transport wrapper over [HttpClient].
 */
internal class HttpTransport(
    private val config: MonobankAcquiringConfig,
) {
    private val logger = LoggerFactory.getLogger(HttpTransport::class.java)

    private val client: HttpClient =
        HttpClient
            .newBuilder()
            .connectTimeout(Duration.ofMillis(config.connectTimeoutMs))
            .build()

    internal fun <T> get(
        path: String,
        responseType: Class<T>,
    ): T {
        val request =
            requestBuilder(path)
                .GET()
                .build()
        val payload = executeForBytes(request)
        return JsonCodec.read(payload.toString(StandardCharsets.UTF_8), responseType)
    }

    internal fun <T> getList(
        path: String,
        elementType: Class<T>,
    ): List<T> {
        val request =
            requestBuilder(path)
                .GET()
                .build()
        val payload = executeForBytes(request)
        return JsonCodec.readList(payload.toString(StandardCharsets.UTF_8), elementType)
    }

    internal fun getBytes(path: String): ByteArray {
        val request =
            requestBuilder(path)
                .GET()
                .build()
        return executeForBytes(request)
    }

    internal fun <T> post(
        path: String,
        requestBody: Any,
        responseType: Class<T>,
    ): T {
        val body = JsonCodec.write(requestBody)
        val request =
            requestBuilder(path)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build()
        val payload = executeForBytes(request)
        val payloadString = payload.toString(StandardCharsets.UTF_8)
        return JsonCodec.read(payloadString, responseType)
    }

    internal fun postUnit(
        path: String,
        requestBody: Any,
    ) {
        val body = JsonCodec.write(requestBody)
        val request =
            requestBuilder(path)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build()
        executeForBytes(request)
    }

    internal fun deleteUnit(path: String) {
        val request =
            requestBuilder(path)
                .DELETE()
                .build()
        executeForBytes(request)
    }

    internal fun encode(value: String): String = URLEncoder.encode(value, StandardCharsets.UTF_8)

    private fun requestBuilder(path: String): HttpRequest.Builder {
        val normalizedBaseUrl = config.baseUrl.removeSuffix("/")
        val normalizedPath = if (path.startsWith("/")) path else "/$path"
        val requestBuilder =
            HttpRequest
                .newBuilder()
                .uri(URI.create(normalizedBaseUrl + normalizedPath))
                .header("X-Token", config.token)
                .timeout(Duration.ofMillis(config.readTimeoutMs))

        config.cmsName?.let { requestBuilder.header("X-Cms", it) }
        config.cmsVersion?.let { requestBuilder.header("X-Cms-Version", it) }
        return requestBuilder
    }

    private fun executeForBytes(request: HttpRequest): ByteArray {
        logger.debug("Monobank request: {} {}", request.method(), request.uri())
        try {
            val response = client.send(request, HttpResponse.BodyHandlers.ofByteArray())
            val statusCode = response.statusCode()
            val bodyBytes = response.body()
            if (statusCode in 200..299) {
                return bodyBytes
            }
            throwApiError(statusCode, bodyBytes)
        } catch (exception: InterruptedException) {
            Thread.currentThread().interrupt()
            throw MonobankNetworkException("Monobank request interrupted", exception)
        } catch (exception: Exception) {
            rethrowOrWrapNetwork(exception)
        }
    }

    private fun throwApiError(
        statusCode: Int,
        bodyBytes: ByteArray,
    ): Nothing {
        val bodyString = bodyBytes.toString(StandardCharsets.UTF_8)
        val error = parseError(bodyString)
        logger.warn("Monobank error response: status={}, body={}", statusCode, bodyString)
        throw when {
            statusCode == 403 -> MonobankAuthException(error?.errCode, error?.errText, bodyString)
            statusCode == 429 -> MonobankRateLimitException(error?.errCode, error?.errText, bodyString)
            statusCode >= 500 -> MonobankNetworkException("Monobank server error: HTTP $statusCode")
            else -> MonobankApiException(statusCode, error?.errCode, error?.errText, bodyString)
        }
    }

    private fun rethrowOrWrapNetwork(exception: Exception): Nothing {
        when (exception) {
            is MonobankApiException,
            is MonobankAuthException,
            is MonobankRateLimitException,
            is MonobankNetworkException,
            -> throw exception
            else -> throw MonobankNetworkException("Monobank network request failed", exception)
        }
    }

    private fun parseError(body: String): ErrorResponse? =
        if (body.isBlank()) {
            null
        } else {
            runCatching { JsonCodec.read(body, ErrorResponse::class.java) }.getOrNull()
        }
}
