package app.spilki.monobank.acquiring.model.common

/**
 * Card tokenization options for invoices.
 *
 * @property saveCard Whether to save card into wallet.
 * @property walletId Target wallet identifier.
 */
public data class SaveCardData(
    val saveCard: Boolean,
    val walletId: String? = null,
)
