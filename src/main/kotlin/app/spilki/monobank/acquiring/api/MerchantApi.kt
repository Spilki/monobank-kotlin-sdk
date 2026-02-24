package app.spilki.monobank.acquiring.api

import app.spilki.monobank.acquiring.internal.HttpTransport
import app.spilki.monobank.acquiring.model.merchant.MerchantDetailsResponse
import app.spilki.monobank.acquiring.model.merchant.PublicKeyResponse
import app.spilki.monobank.acquiring.model.merchant.StatementItem

/**
 * API client for merchant profile and statements.
 *
 * @constructor Internal constructor used by [app.spilki.monobank.acquiring.MonobankAcquiring].
 */
public class MerchantApi internal constructor(
    private val transport: HttpTransport,
) {
    /**
     * Fetches merchant details.
     *
     * @return Merchant details payload.
     */
    public fun details(): MerchantDetailsResponse =
        transport.get("/api/merchant/details", MerchantDetailsResponse::class.java)

    /**
     * Fetches merchant public key for webhook verification.
     *
     * @return Public key response.
     */
    public fun publicKey(): PublicKeyResponse = transport.get("/api/merchant/pubkey", PublicKeyResponse::class.java)

    /**
     * Loads statement entries for an interval.
     *
     * @param from Start Unix timestamp in seconds.
     * @param to End Unix timestamp in seconds.
     * @return Statement items.
     */
    public fun statement(
        from: Long,
        to: Long,
    ): List<StatementItem> = transport.getList("/api/merchant/statement?from=$from&to=$to", StatementItem::class.java)
}
