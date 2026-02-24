package app.spilki.monobank.acquiring.model.wallet

/**
 * Request descriptor for wallet card deletion.
 *
 * @property cardToken Card token.
 * @property walletId Wallet identifier.
 */
public data class DeleteWalletCardRequest(
    val cardToken: String,
    val walletId: String,
)
