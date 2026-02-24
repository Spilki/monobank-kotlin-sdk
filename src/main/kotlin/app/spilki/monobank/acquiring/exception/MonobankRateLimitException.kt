package app.spilki.monobank.acquiring.exception

/**
 * Exception for API rate limit responses.
 *
 * @property errCode Monobank business error code when present.
 * @property errText Monobank error text when present.
 * @property responseBody Raw response body.
 */
public class MonobankRateLimitException(
    errCode: String?,
    errText: String?,
    responseBody: String?,
) : MonobankApiException(
        statusCode = 429,
        errCode = errCode,
        errText = errText,
        responseBody = responseBody,
    )
