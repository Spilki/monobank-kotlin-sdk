package app.spilki.monobank.acquiring.model.wallet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Tokenized wallet card.
 *
 * @property cardToken Card token.
 * @property maskedPan Masked PAN value.
 * @property country Issuer country code.
 * @property walletId Wallet identifier.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class WalletCard(
    val cardToken: String,
    val maskedPan: String? = null,
    val country: String? = null,
    val walletId: String? = null,
)
