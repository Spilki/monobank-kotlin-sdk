package ua.monobank.acquiring.model.qr

/**
 * Request body to reset QR amount.
 *
 * @property qrId QR identifier.
 */
public data class ResetQrAmountRequest(
    val qrId: String,
)
