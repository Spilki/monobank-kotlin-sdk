package app.spilki.monobank.acquiring.model.invoice

import app.spilki.monobank.acquiring.model.common.CancelStatus
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Response of hold finalization.
 *
 * @property status Finalization status.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class FinalizeInvoiceResponse(
    val status: CancelStatus,
)
