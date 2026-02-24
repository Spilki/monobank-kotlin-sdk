package ua.monobank.acquiring.model.monopay

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Monopay public key metadata item.
 *
 * @property keyId Key identifier.
 * @property key Public key payload.
 * @property name Human-readable alias.
 * @property createdDate ISO-8601 creation timestamp.
 * @property status Status value.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class PubKeyItem(
    val keyId: String? = null,
    val key: String? = null,
    val name: String? = null,
    val createdDate: String? = null,
    val status: String? = null,
)
