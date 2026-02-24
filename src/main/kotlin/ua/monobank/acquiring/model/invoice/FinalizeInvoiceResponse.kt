package ua.monobank.acquiring.model.invoice

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ua.monobank.acquiring.model.common.CancelStatus

/**
 * Response of hold finalization.
 *
 * @property status Finalization status.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class FinalizeInvoiceResponse(
    val status: CancelStatus,
)
