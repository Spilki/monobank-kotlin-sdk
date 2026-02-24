package ua.monobank.acquiring.exception

/**
 * Base SDK exception for Monobank Acquiring errors.
 *
 * @property message Human-readable error message.
 * @property cause Underlying cause when available.
 */
public sealed class MonobankException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
