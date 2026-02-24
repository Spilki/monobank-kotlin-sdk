package ua.monobank.acquiring.model.invoice

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Fiscal check record associated with an invoice.
 *
 * @property checkId Fiscal check identifier.
 * @property status Fiscal check status.
 * @property type Fiscal check type.
 * @property createdDate ISO-8601 creation timestamp.
 * @property url URL to check document.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class FiscalCheck(
    val checkId: String? = null,
    val status: String? = null,
    val type: String? = null,
    val createdDate: String? = null,
    val url: String? = null,
)
