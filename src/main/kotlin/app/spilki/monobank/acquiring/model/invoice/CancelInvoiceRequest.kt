package app.spilki.monobank.acquiring.model.invoice

import app.spilki.monobank.acquiring.model.common.BasketItem

/**
 * Request to cancel an invoice fully or partially.
 *
 * @property invoiceId Invoice identifier.
 * @property extRef Optional external reference for cancel action.
 * @property amount Optional partial cancel amount in minor units.
 * @property items Optional item list for fiscalized partial cancel.
 */
public data class CancelInvoiceRequest(
    val invoiceId: String,
    val extRef: String? = null,
    val amount: Long? = null,
    val items: List<BasketItem>? = null,
)
