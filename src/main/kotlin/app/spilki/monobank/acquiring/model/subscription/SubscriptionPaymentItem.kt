package app.spilki.monobank.acquiring.model.subscription

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Single subscription payment attempt.
 *
 * @property invoiceId Related invoice identifier.
 * @property trnId Transaction identifier.
 * @property status Payment status.
 * @property amount Amount in minor units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property createdDate ISO-8601 creation timestamp.
 * @property modifiedDate ISO-8601 update timestamp.
 * @property failureReason Failure reason text.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class SubscriptionPaymentItem(
    val invoiceId: String? = null,
    val trnId: String? = null,
    val status: String? = null,
    val amount: Long? = null,
    val ccy: Int? = null,
    val createdDate: String? = null,
    val modifiedDate: String? = null,
    val failureReason: String? = null,
)
