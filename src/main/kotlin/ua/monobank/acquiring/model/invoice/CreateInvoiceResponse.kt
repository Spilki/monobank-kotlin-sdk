package ua.monobank.acquiring.model.invoice

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Response for invoice creation endpoint.
 *
 * @property invoiceId Created invoice identifier.
 * @property pageUrl Hosted payment page URL.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class CreateInvoiceResponse(
    val invoiceId: String,
    val pageUrl: String,
)
