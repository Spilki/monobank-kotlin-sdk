package ua.monobank.acquiring.model.wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ua.monobank.acquiring.model.common.InvoiceStatus

/**
 * Response from wallet payment endpoint.
 *
 * @property invoiceId Invoice identifier.
 * @property status Payment status.
 * @property trnId Transaction identifier.
 * @property amount Processed amount in minor units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property errCode Business error code.
 * @property errText Business error text.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class WalletPaymentResponse(
    val invoiceId: String? = null,
    val status: InvoiceStatus? = null,
    val trnId: String? = null,
    val amount: Long? = null,
    val ccy: Int? = null,
    val errCode: String? = null,
    val errText: String? = null,
)
