package app.spilki.monobank.acquiring.model.invoice

/**
 * Request body to remove invoice from merchant cabinet.
 *
 * @property invoiceId Invoice identifier.
 */
public data class RemoveInvoiceRequest(
    val invoiceId: String,
)
