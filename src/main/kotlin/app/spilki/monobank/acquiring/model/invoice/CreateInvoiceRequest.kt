package app.spilki.monobank.acquiring.model.invoice

import app.spilki.monobank.acquiring.model.common.MerchantPaymInfo
import app.spilki.monobank.acquiring.model.common.PaymentType
import app.spilki.monobank.acquiring.model.common.SaveCardData

/**
 * Request body for invoice creation endpoint.
 *
 * @property amount Invoice amount in minor currency units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property merchantPaymInfo Merchant payment metadata.
 * @property redirectUrl URL to redirect user after payment.
 * @property webHookUrl Webhook URL for status callbacks.
 * @property validity Invoice validity in seconds.
 * @property paymentType Payment mode.
 * @property saveCardData Card tokenization settings.
 * @property qrId Existing QR identifier for invoice binding.
 * @property extRef External merchant reference for idempotency.
 * @property code Optional invoice code.
 * @property language Preferred invoice page language.
 * @property tipsEmployeeId Employee identifier for tips flow.
 */
public data class CreateInvoiceRequest(
    val amount: Long,
    val ccy: Int,
    val merchantPaymInfo: MerchantPaymInfo? = null,
    val redirectUrl: String? = null,
    val webHookUrl: String? = null,
    val validity: Long? = null,
    val paymentType: PaymentType? = null,
    val saveCardData: SaveCardData? = null,
    val qrId: String? = null,
    val extRef: String? = null,
    val code: String? = null,
    val language: String? = null,
    val tipsEmployeeId: String? = null,
)
