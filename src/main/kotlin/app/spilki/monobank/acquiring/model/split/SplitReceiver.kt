package app.spilki.monobank.acquiring.model.split

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Split payment receiver settings.
 *
 * @property receiverId Receiver identifier.
 * @property name Receiver name.
 * @property iban Receiver IBAN.
 * @property edrpou Receiver EDRPOU code.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class SplitReceiver(
    val receiverId: String? = null,
    val name: String? = null,
    val iban: String? = null,
    val edrpou: String? = null,
)
