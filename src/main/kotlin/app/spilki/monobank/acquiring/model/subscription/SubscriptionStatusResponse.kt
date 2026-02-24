package app.spilki.monobank.acquiring.model.subscription

import app.spilki.monobank.acquiring.model.common.PaymentType
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Subscription state snapshot.
 *
 * @property subId Subscription identifier.
 * @property status Current status value.
 * @property amount Recurring amount in minor units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property createdDate ISO-8601 creation timestamp.
 * @property modifiedDate ISO-8601 update timestamp.
 * @property nextPaymentDate ISO-8601 next payment timestamp.
 * @property startDate ISO-8601 subscription start timestamp.
 * @property endDate ISO-8601 subscription end timestamp.
 * @property walletId Wallet identifier.
 * @property paymentType Payment type.
 * @property failureReason Failure reason text.
 * @property errCode Business error code.
 * @property errText Business error text.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class SubscriptionStatusResponse(
    val subId: String,
    val status: String? = null,
    val amount: Long? = null,
    val ccy: Int? = null,
    val createdDate: String? = null,
    val modifiedDate: String? = null,
    val nextPaymentDate: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val walletId: String? = null,
    val paymentType: PaymentType? = null,
    val failureReason: String? = null,
    val errCode: String? = null,
    val errText: String? = null,
)
