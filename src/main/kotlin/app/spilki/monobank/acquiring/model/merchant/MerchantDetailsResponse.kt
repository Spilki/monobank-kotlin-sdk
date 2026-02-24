package app.spilki.monobank.acquiring.model.merchant

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Merchant profile information.
 *
 * @property merchantId Merchant identifier.
 * @property merchantName Merchant display name.
 * @property edrpou Merchant EDRPOU code.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class MerchantDetailsResponse(
    val merchantId: String,
    val merchantName: String,
    val edrpou: String,
)
