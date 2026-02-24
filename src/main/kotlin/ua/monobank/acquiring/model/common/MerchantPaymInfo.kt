package ua.monobank.acquiring.model.common

/**
 * Merchant payment metadata.
 *
 * @property reference Merchant-side reference identifier.
 * @property destination Payment destination text shown to user.
 * @property comment Additional payment comment.
 * @property basketOrder Itemized basket for receipts.
 */
public data class MerchantPaymInfo(
    val reference: String? = null,
    val destination: String? = null,
    val comment: String? = null,
    val basketOrder: List<BasketItem>? = null,
)
