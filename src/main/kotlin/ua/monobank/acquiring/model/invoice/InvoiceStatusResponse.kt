package ua.monobank.acquiring.model.invoice

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ua.monobank.acquiring.model.common.CancelListItem
import ua.monobank.acquiring.model.common.InvoiceStatus
import ua.monobank.acquiring.model.common.PaymentInfo
import ua.monobank.acquiring.model.wallet.WalletCard

/**
 * Full invoice status response.
 *
 * @property invoiceId Invoice identifier.
 * @property status Current invoice status.
 * @property amount Invoice amount in minor units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property finalAmount Final captured amount in minor units.
 * @property createdDate ISO-8601 creation timestamp.
 * @property modifiedDate ISO-8601 update timestamp.
 * @property failureReason Failure reason text.
 * @property paymentInfo Card payment details.
 * @property walletData Wallet card metadata used in payment.
 * @property cancelList List of cancel operations.
 * @property reference Merchant reference.
 * @property destination Destination text.
 * @property errCode Business error code.
 * @property errText Business error text.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class InvoiceStatusResponse(
    val invoiceId: String,
    val status: InvoiceStatus,
    val amount: Long,
    val ccy: Int,
    val finalAmount: Long? = null,
    val createdDate: String? = null,
    val modifiedDate: String? = null,
    val failureReason: String? = null,
    val paymentInfo: PaymentInfo? = null,
    val walletData: WalletCard? = null,
    val cancelList: List<CancelListItem>? = null,
    val reference: String? = null,
    val destination: String? = null,
    val errCode: String? = null,
    val errText: String? = null,
)
