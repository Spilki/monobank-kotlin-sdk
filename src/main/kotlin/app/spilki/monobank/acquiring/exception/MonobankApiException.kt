package app.spilki.monobank.acquiring.exception

/**
 * Exception for API-level error responses.
 *
 * @property statusCode HTTP status code.
 * @property errCode Monobank business error code when present.
 * @property errText Monobank error text when present.
 * @property responseBody Raw response body.
 */
public open class MonobankApiException(
    public val statusCode: Int,
    public val errCode: String?,
    public val errText: String?,
    public val responseBody: String?,
) : MonobankException(
        message =
            "Monobank API error: HTTP $statusCode" +
                if (errCode != null || errText != null) " ($errCode: $errText)" else "",
    )
