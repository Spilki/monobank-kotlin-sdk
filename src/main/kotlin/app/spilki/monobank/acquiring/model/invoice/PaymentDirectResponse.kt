package app.spilki.monobank.acquiring.model.invoice

import app.spilki.monobank.acquiring.model.common.InvoiceStatus
import app.spilki.monobank.acquiring.model.common.PaymentInfo
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Response from payment-direct endpoint.
 *
 * @property invoiceId Invoice identifier.
 * @property status Payment status.
 * @property trnId Transaction identifier.
 * @property amount Processed amount in minor units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property paymentInfo Card payment details.
 * @property failureReason Failure reason text.
 * @property errCode Business error code.
 * @property errText Business error text.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class PaymentDirectResponse(
    val invoiceId: String? = null,
    val status: InvoiceStatus? = null,
    val trnId: String? = null,
    val amount: Long? = null,
    val ccy: Int? = null,
    val paymentInfo: PaymentInfo? = null,
    val failureReason: String? = null,
    val errCode: String? = null,
    val errText: String? = null,
)
