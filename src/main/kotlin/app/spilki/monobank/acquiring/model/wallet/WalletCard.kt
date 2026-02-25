package app.spilki.monobank.acquiring.model.wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Tokenized wallet card.
 *
 * @property cardToken Card token (null for wallets in early statuses).
 * @property maskedPan Masked PAN value.
 * @property country Issuer country code.
 * @property walletId Wallet identifier.
 * @property status Wallet status (e.g. "new", "created").
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class WalletCard(
    val cardToken: String? = null,
    val maskedPan: String? = null,
    val country: String? = null,
    val walletId: String? = null,
    val status: String? = null,
)
