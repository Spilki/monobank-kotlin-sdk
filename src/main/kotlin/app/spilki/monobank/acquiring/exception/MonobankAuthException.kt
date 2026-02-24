package app.spilki.monobank.acquiring.exception

/**
 * Exception for authentication and authorization errors.
 *
 * @property errCode Monobank business error code when present.
 * @property errText Monobank error text when present.
 * @property responseBody Raw response body.
 */
public class MonobankAuthException(
    errCode: String?,
    errText: String?,
    responseBody: String?,
) : MonobankApiException(
        statusCode = 403,
        errCode = errCode,
        errText = errText,
        responseBody = responseBody,
    )
