package ua.monobank.acquiring.model.merchant

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Public key response used for webhook signature verification.
 *
 * @property key Base64-encoded PEM public key.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class PublicKeyResponse(
    val key: String,
)
