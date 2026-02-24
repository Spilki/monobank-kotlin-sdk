package ua.monobank.acquiring.model.submerchant

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Submerchant descriptor.
 *
 * @property submerchantId Submerchant identifier.
 * @property name Submerchant name.
 * @property edrpou Submerchant EDRPOU code.
 * @property mcc Submerchant MCC.
 * @property status Status value.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class Submerchant(
    val submerchantId: String? = null,
    val name: String? = null,
    val edrpou: String? = null,
    val mcc: Int? = null,
    val status: String? = null,
)
