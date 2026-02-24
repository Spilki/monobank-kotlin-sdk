package ua.monobank.acquiring.model.qr

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * QR details response.
 *
 * @property qrId QR identifier.
 * @property amount Current amount in minor units.
 * @property ccy Numeric ISO 4217 currency code.
 * @property shortQrId Short QR identifier.
 * @property pageUrl Hosted payment page URL.
 * @property createdDate ISO-8601 creation timestamp.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class QrDetailsResponse(
    val qrId: String,
    val amount: Long? = null,
    val ccy: Int? = null,
    val shortQrId: String? = null,
    val pageUrl: String? = null,
    val createdDate: String? = null,
)
