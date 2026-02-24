package app.spilki.monobank.acquiring.model.wallet

import app.spilki.monobank.acquiring.model.common.MerchantPaymInfo
import app.spilki.monobank.acquiring.model.common.PaymentType

/**
 * Request body for wallet payment.
 *
 * @property cardToken Card token.
 * @property amount Amount in minor units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property merchantPaymInfo Merchant payment metadata.
 * @property redirectUrl Redirect URL after payment.
 * @property webHookUrl Webhook URL for status callbacks.
 * @property initiationKind Initiation kind value.
 * @property paymentType Payment mode.
 */
public data class WalletPaymentRequest(
    val cardToken: String,
    val amount: Long,
    val ccy: Int,
    val merchantPaymInfo: MerchantPaymInfo? = null,
    val redirectUrl: String? = null,
    val webHookUrl: String? = null,
    val initiationKind: String? = null,
    val paymentType: PaymentType? = null,
)
