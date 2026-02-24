package app.spilki.monobank.acquiring.api

import app.spilki.monobank.acquiring.internal.HttpTransport
import app.spilki.monobank.acquiring.model.wallet.WalletCard
import app.spilki.monobank.acquiring.model.wallet.WalletPaymentRequest
import app.spilki.monobank.acquiring.model.wallet.WalletPaymentResponse

/**
 * API client for wallet tokenized-card operations.
 *
 * @constructor Internal constructor used by [app.spilki.monobank.acquiring.MonobankAcquiring].
 */
public class WalletApi internal constructor(
    private val transport: HttpTransport,
) {
    /**
     * Loads tokenized cards in wallet.
     *
     * @param walletId Wallet identifier.
     * @return Wallet cards.
     */
    public fun cards(walletId: String): List<WalletCard> =
        transport.getList("/api/merchant/wallet?walletId=${transport.encode(walletId)}", WalletCard::class.java)

    /**
     * Charges tokenized wallet card.
     *
     * @param request Wallet payment payload.
     * @return Wallet payment response.
     */
    public fun payment(request: WalletPaymentRequest): WalletPaymentResponse =
        transport.post("/api/merchant/wallet/payment", request, WalletPaymentResponse::class.java)

    /**
     * Deletes tokenized wallet card.
     *
     * @param cardToken Card token.
     * @param walletId Wallet identifier.
     */
    public fun deleteCard(
        cardToken: String,
        walletId: String,
    ): Unit =
        transport.deleteUnit(
            "/api/merchant/wallet/card?cardToken=${transport.encode(cardToken)}&walletId=${transport.encode(walletId)}",
        )
}
