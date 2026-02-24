package ua.monobank.acquiring

import ua.monobank.acquiring.api.InvoiceApi
import ua.monobank.acquiring.api.MerchantApi
import ua.monobank.acquiring.api.MonopayApi
import ua.monobank.acquiring.api.QrApi
import ua.monobank.acquiring.api.SplitApi
import ua.monobank.acquiring.api.SubmerchantApi
import ua.monobank.acquiring.api.SubscriptionApi
import ua.monobank.acquiring.api.TerminalApi
import ua.monobank.acquiring.api.WalletApi
import ua.monobank.acquiring.internal.HttpTransport
import ua.monobank.acquiring.webhook.WebhookVerifier

/**
 * Main facade for Monobank Acquiring SDK.
 *
 * @constructor Creates SDK clients from [config].
 * @property invoices Invoice API client.
 * @property merchant Merchant API client.
 * @property subscriptions Subscription API client.
 * @property qr QR API client.
 * @property wallet Wallet API client.
 * @property split Split API client.
 * @property monopay Monopay API client.
 * @property submerchants Submerchant API client.
 * @property terminals Terminal API client.
 * @property webhooks Webhook signature verifier.
 */
public class MonobankAcquiring(
    config: MonobankAcquiringConfig,
) {
    private val transport: HttpTransport = HttpTransport(config)

    /** Invoice operations facade. */
    public val invoices: InvoiceApi = InvoiceApi(transport)

    /** Merchant operations facade. */
    public val merchant: MerchantApi = MerchantApi(transport)

    /** Subscription operations facade. */
    public val subscriptions: SubscriptionApi = SubscriptionApi(transport)

    /** QR operations facade. */
    public val qr: QrApi = QrApi(transport)

    /** Wallet operations facade. */
    public val wallet: WalletApi = WalletApi(transport)

    /** Split receiver operations facade. */
    public val split: SplitApi = SplitApi(transport)

    /** Monopay operations facade. */
    public val monopay: MonopayApi = MonopayApi(transport)

    /** Submerchant operations facade. */
    public val submerchants: SubmerchantApi = SubmerchantApi(transport)

    /** Terminal operations facade. */
    public val terminals: TerminalApi = TerminalApi(transport)

    /** Webhook verification helper. */
    public val webhooks: WebhookVerifier = WebhookVerifier(merchant)
}
