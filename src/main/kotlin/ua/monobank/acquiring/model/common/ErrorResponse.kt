package ua.monobank.acquiring.model.common

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Standard error response from Monobank API.
 *
 * @property errCode Business error code.
 * @property errText Human-readable error text.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class ErrorResponse(
    val errCode: String? = null,
    val errText: String? = null,
)
