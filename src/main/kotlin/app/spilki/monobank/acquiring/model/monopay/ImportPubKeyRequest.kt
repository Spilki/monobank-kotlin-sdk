package app.spilki.monobank.acquiring.model.monopay

/**
 * Request body for Monopay public key import.
 *
 * @property key Public key value.
 * @property name Public key alias.
 */
public data class ImportPubKeyRequest(
    val key: String,
    val name: String? = null,
)
