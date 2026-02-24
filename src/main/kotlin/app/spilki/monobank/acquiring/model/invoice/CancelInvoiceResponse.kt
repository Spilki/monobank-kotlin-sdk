package app.spilki.monobank.acquiring.model.invoice

import app.spilki.monobank.acquiring.model.common.CancelStatus
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Response of invoice cancel operation.
 *
 * @property status Operation status.
 * @property createdDate ISO-8601 creation timestamp.
 * @property modifiedDate ISO-8601 update timestamp.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class CancelInvoiceResponse(
    val status: CancelStatus,
    val createdDate: String? = null,
    val modifiedDate: String? = null,
)
