package ua.monobank.acquiring.model.invoice

import ua.monobank.acquiring.model.common.BasketItem

/**
 * Request to finalize a hold invoice.
 *
 * @property invoiceId Invoice identifier.
 * @property amount Optional amount to capture in minor units.
 * @property items Optional basket for fiscal finalization.
 */
public data class FinalizeInvoiceRequest(
    val invoiceId: String,
    val amount: Long? = null,
    val items: List<BasketItem>? = null,
)
