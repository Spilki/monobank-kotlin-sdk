package ua.monobank.acquiring.exception

/**
 * Exception for transport errors and server-side failures.
 *
 * @property message Human-readable error message.
 * @property cause Underlying exception if present.
 */
public class MonobankNetworkException(
    message: String,
    cause: Throwable? = null,
) : MonobankException(message, cause)
